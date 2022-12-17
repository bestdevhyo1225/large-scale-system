package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POSTS_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.mapper.PostCacheDtoMapper
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postRedisService: PostRedisService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POSTS_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val postCacheDtos: List<PostCacheDto> =
            postRedisReadService.findPostCaches(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        if (postCacheDtos.isNotEmpty() && postCacheDtos.size == pageRequestByPosition.size.toInt()) {
            val postDtos: List<PostDto> = postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }
            return PageByPosition(
                items = postDtos,
                nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
            )
        }

        val postIdMapByPostCacheDtos: Map<Long, Boolean> = postCacheDtos.associate { it.id to true }
        val pageByPositionPostDto: PageByPosition<PostDto> =
            postReadService.findPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        val createPostCacheDto: List<PostCacheDto> = pageByPositionPostDto.items
            .filter { postIdMapByPostCacheDtos[it.id] == null || postIdMapByPostCacheDtos[it.id] == false }
            .map { PostCacheDtoMapper.of(postDto = it) }

        CoroutineScope(context = Dispatchers.IO).launch {
            createPostCacheDto.forEach { postRedisService.createOrUpdate(dto = it) }
        }

        return pageByPositionPostDto
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 게시물들을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

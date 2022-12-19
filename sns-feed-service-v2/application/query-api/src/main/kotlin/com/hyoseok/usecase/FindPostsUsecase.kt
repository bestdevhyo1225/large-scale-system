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
        val (start: Long, size: Long) = pageRequestByPosition

        if (start < 0 || size == 0L) {
            return PageByPosition(items = listOf(), nextPageRequestByPosition = pageRequestByPosition)
        }

        val (postCacheDtos: List<PostCacheDto>, notExistsPostIds: List<Long>) =
            postRedisReadService.findPostCaches(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        if (postCacheDtos.isEmpty()) {
            return findPostsAndCreatePostCaches(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        }

        if (notExistsPostIds.isEmpty()) {
            return getPostsByPostCaches(postCacheDtos = postCacheDtos, pageRequestByPosition = pageRequestByPosition)
        }

        return findPostsAndCreatePostCaches(
            notExistsPostIds = notExistsPostIds,
            postCacheDtos = postCacheDtos,
            pageRequestByPosition = pageRequestByPosition,
        )
    }

    private fun findPostsAndCreatePostCaches(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): PageByPosition<PostDto> {
        val pageByPositionPostDto: PageByPosition<PostDto> =
            postReadService.findPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        CoroutineScope(context = Dispatchers.IO).launch {
            pageByPositionPostDto.items.forEach {
                postRedisService.createOrUpdate(dto = PostCacheDtoMapper.of(postDto = it))
            }
        }

        return pageByPositionPostDto
    }

    private fun getPostsByPostCaches(
        postCacheDtos: List<PostCacheDto>,
        pageRequestByPosition: PageRequestByPosition,
    ): PageByPosition<PostDto> {
        val postDtos: List<PostDto> = postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun findPostsAndCreatePostCaches(
        notExistsPostIds: List<Long>,
        postCacheDtos: List<PostCacheDto>,
        pageRequestByPosition: PageRequestByPosition,
    ): PageByPosition<PostDto> {
        val postDtos: List<PostDto> = postReadService.findPosts(ids = notExistsPostIds)
        val appendedPostDtos: List<PostDto> = postDtos.plus(postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) })

        CoroutineScope(context = Dispatchers.IO).launch {
            postDtos.forEach {
                postRedisService.createOrUpdate(dto = PostCacheDtoMapper.of(postDto = it))
            }
        }

        return PageByPosition(
            items = appendedPostDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = appendedPostDtos.size),
        )
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 게시물들을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

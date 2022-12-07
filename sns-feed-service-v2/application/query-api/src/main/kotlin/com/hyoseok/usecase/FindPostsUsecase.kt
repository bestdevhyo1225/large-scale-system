package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POSTS_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POSTS_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val postCacheDtos: List<PostCacheDto> =
            postRedisReadService.findPostCaches(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        if (postCacheDtos.isEmpty() || postCacheDtos.size < pageRequestByPosition.size) {
            return postReadService.findPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        }

        val postDtos: List<PostDto> = postCacheDtos.map { createPostDto(postCacheDto = it) }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun createPostDto(postCacheDto: PostCacheDto): PostDto =
        with(receiver = postCacheDto) {
            PostDto(
                id = id,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = images.map { PostImageDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
            )
        }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 게시물 리스트를 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

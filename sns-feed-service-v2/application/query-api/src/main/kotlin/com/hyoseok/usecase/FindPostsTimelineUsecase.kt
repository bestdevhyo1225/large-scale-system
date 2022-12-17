package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsTimelineUsecase(
    private val feedRedisReadService: FeedRedisReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val feedDtos: List<FeedDto> =
            feedRedisReadService.findFeeds(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        val postIds: List<Long> = feedDtos.map { it.postId }
        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)

        val postDtos: List<PostDto> =
            if (postCacheDtos.isNotEmpty()) {
                postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }
            } else {
                postReadService.findPosts(ids = postIds)
            }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

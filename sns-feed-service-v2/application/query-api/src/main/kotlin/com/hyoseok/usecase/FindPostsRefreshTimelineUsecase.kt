package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_REFRESH_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.service.FeedRedisService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FindPostsRefreshTimelineUsecase(
    private val feedRedisService: FeedRedisService,
    private val followReadService: FollowReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_REFRESH_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long) {
        val findFolloweeMaxLimit: Long = 1_000
        val followeeIds: List<Long> = followReadService.findInfluencerFolloweeIds(
            followerId = memberId,
            findFolloweeMaxLimit = findFolloweeMaxLimit,
        )
        val toCreatedAt: LocalDateTime = LocalDateTime.now().withNano(0)
        val fromCreatedAt: LocalDateTime = toCreatedAt.minusMinutes(10)
        val postLimit: Long = 1_000
        val postOffset: Long = 0
        val postDtos: List<PostDto> = postReadService.findPosts(
            memberIds = followeeIds,
            fromCreatedAt = fromCreatedAt,
            toCreatedAt = toCreatedAt,
            limit = postLimit,
            offset = postOffset,
        )

        postDtos.forEach {
            feedRedisService.create(memberId = memberId, postId = it.id)
        }
    }

    private fun fallbackExecute(exception: Exception) {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 새로고침 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

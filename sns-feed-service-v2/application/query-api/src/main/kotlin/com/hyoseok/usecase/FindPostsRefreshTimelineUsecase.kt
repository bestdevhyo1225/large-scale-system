package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_REFRESH_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.service.FeedRedisService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.member.service.MemberService
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FindPostsRefreshTimelineUsecase(
    private val feedRedisService: FeedRedisService,
    private val followReadService: FollowReadService,
    private val memberService: MemberService,
    private val memberReadService: MemberReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_REFRESH_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long) {
        val followeeIds: List<Long> = followReadService.findInfluencerFolloweeIds(
            followerId = memberId,
            findFolloweeMaxLimit = 1_000,
        )
        val memberDto: MemberDto = memberReadService.findMember(id = memberId)
        val postDtos: List<PostDto> = postReadService.findPosts(
            memberIds = followeeIds,
            fromCreatedAt = memberDto.lastLoginDatetime, // 마지막 접속 날짜
            toCreatedAt = LocalDateTime.now().withNano(0),
            limit = 1_000,
            offset = 0,
        )

        postDtos.forEach {
            feedRedisService.create(memberId = memberId, postId = it.id)
        }

        CoroutineScope(context = Dispatchers.IO).launch {
            memberService.updateLastLoginDatetime(memberId = memberId)
        }
    }

    private fun fallbackExecute(exception: Exception) {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 새로고침 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

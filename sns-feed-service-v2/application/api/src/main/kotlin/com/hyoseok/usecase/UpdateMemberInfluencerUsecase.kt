package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.UPDATE_MEMBER_INFLUENCER_USECASE
import com.hyoseok.exception.ApiRateLimitException
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.service.MemberService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UpdateMemberInfluencerUsecase(
    private val followReadService: FollowReadService,
    private val memberService: MemberService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = UPDATE_MEMBER_INFLUENCER_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long) {
        followReadService.checkInfluencer(followeeId = memberId)
        memberService.updateInfluener(memberId = memberId)
    }

    private fun fallbackExecute(exception: Exception) {
        logger.error { exception.localizedMessage }
        throw ApiRateLimitException(message = "일시적으로 인스타그램 계정 변경을 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

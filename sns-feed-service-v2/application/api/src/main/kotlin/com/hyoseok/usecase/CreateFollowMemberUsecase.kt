package com.hyoseok.usecase

import com.hyoseok.exception.ApiRateLimitException
import com.hyoseok.follow.dto.FollowCreateDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.service.FollowService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CreateFollowMemberUsecase(
    private val followService: FollowService,
    private val memberReadService: MemberReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = "createFollowMemberUsecase", fallbackMethod = "fallbackExecute")
    fun execute(followerId: Long, followeeId: Long): FollowDto {
        val followerMemberDto: MemberDto = memberReadService.findMember(id = followerId)
        val followeeMemberDto: MemberDto = memberReadService.findMember(id = followeeId)
        val followCreateDto = FollowCreateDto(followerId = followerMemberDto.id, followeeId = followeeMemberDto.id)
        return followService.create(dto = followCreateDto)
    }

    private fun fallbackExecute(exception: Exception): FollowDto {
        logger.error { exception.localizedMessage }
        throw ApiRateLimitException(message = "일시적으로 팔로우를 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

package com.hyoseok.usecase

import com.hyoseok.service.dto.FollowCreateResultDto
import com.hyoseok.service.follow.FollowService
import com.hyoseok.service.member.MemberReadService
import org.springframework.stereotype.Service

@Service
class CreateFollowMemberUsecase(
    private val followService: FollowService,
    private val memberReadService: MemberReadService,
) {

    fun execute(followerId: Long, followeeId: Long): FollowCreateResultDto =
        followService.following(
            followerMemberDto = memberReadService.findMember(id = followerId),
            followeeMemberDto = memberReadService.findMember(id = followeeId),
        )
}

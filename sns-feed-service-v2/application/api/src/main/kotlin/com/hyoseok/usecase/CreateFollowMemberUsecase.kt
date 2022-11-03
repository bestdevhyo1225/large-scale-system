package com.hyoseok.usecase

import com.hyoseok.follow.dto.FollowCreateDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.service.FollowService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import org.springframework.stereotype.Service

@Service
class CreateFollowMemberUsecase(
    private val followService: FollowService,
    private val memberReadService: MemberReadService,
) {

    fun execute(followerId: Long, followeeId: Long): FollowDto {
        val followerMemberDto: MemberDto = memberReadService.findMember(id = followerId)
        val followeeMemberDto: MemberDto = memberReadService.findMember(id = followeeId)
        return followService.create(
            dto = FollowCreateDto(followerId = followerMemberDto.id, followeeId = followeeMemberDto.id),
        )
    }
}

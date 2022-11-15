package com.hyoseok.usecase

import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.service.MemberService
import org.springframework.stereotype.Service

@Service
class UpdateMemberInfluencerUsecase(
    private val followReadService: FollowReadService,
    private val memberService: MemberService,
) {

    fun execute(memberId: Long) {
        val followerCount: Long = followReadService.getFollowerCount(followeeId = memberId)
        memberService.updateInfluenerAccount(memberId = memberId, followerCount = followerCount)
    }
}

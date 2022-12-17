package com.hyoseok.usecase

import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.service.MemberService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.justRun
import io.mockk.mockk

internal class UpdateMemberInfluencerUsecaseTests : BehaviorSpec(
    {
        val mockFollowReadService: FollowReadService = mockk()
        val mockMemberService: MemberService = mockk()
        val updateMemberInfluencerUsecase = UpdateMemberInfluencerUsecase(
            followReadService = mockFollowReadService,
            memberService = mockMemberService,
        )

        given("계정을 인플루언서로 변경하기 위해 아래와 같은 상황이 주어지면") {
            val followeeId = 1L

            justRun { mockFollowReadService.checkInfluencer(followeeId = followeeId) }
            justRun { mockMemberService.updateInfluener(memberId = followeeId) }

            `when`("계정을 인플루언서로 변경하는데") {
                updateMemberInfluencerUsecase.execute(memberId = followeeId)
            }
        }
    },
)

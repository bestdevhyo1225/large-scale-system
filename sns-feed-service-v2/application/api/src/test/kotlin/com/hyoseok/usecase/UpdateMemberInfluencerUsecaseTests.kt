package com.hyoseok.usecase

import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.service.MemberService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify

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
            val followerCount = 10_001L

            every { mockFollowReadService.getFollowerCount(followeeId = followeeId) } returns followerCount
            justRun {
                mockMemberService.updateInfluenerAccount(
                    memberId = followeeId,
                    followerCount = followerCount,
                )
            }

            `when`("계정을 인플루언서로 변경하는데") {
                updateMemberInfluencerUsecase.execute(memberId = followeeId)

                then("이와 관련된 메서드들은 최소 1번씩 호출된다") {
                    verify { mockFollowReadService.getFollowerCount(followeeId = followeeId) }
                }
            }
        }
    },
)

package com.hyoseok.usecase

import com.hyoseok.follow.dto.FollowCreateDto
import com.hyoseok.follow.dto.FollowDto
import com.hyoseok.follow.service.FollowService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class CreateFollowMemberUsecaseTests : BehaviorSpec(
    {
        val mockFollowService: FollowService = mockk()
        val mockMemberReadService: MemberReadService = mockk()
        val createFollowMemberUsecase = CreateFollowMemberUsecase(
            followService = mockFollowService,
            memberReadService = mockMemberReadService,
        )

        given("팔로우를 할 때") {
            `when`("팔로이와 팔로워를 조회한 다음, 팔로우를 처리하고") {
                val followerMemberDto = MemberDto(
                    id = 1L,
                    name = "팔로워",
                    influencer = false,
                    createdAt = LocalDateTime.now().withNano(0),
                )
                val followeeMemberDto = MemberDto(
                    id = 2L,
                    name = "팔로이",
                    influencer = false,
                    createdAt = LocalDateTime.now().withNano(0),
                )
                val followCreateDto = FollowCreateDto(
                    followerId = followerMemberDto.id,
                    followeeId = followeeMemberDto.id,
                )

                every { mockMemberReadService.findMember(id = followerMemberDto.id) } returns followerMemberDto
                every { mockMemberReadService.findMember(id = followeeMemberDto.id) } returns followeeMemberDto
                every { mockFollowService.create(dto = followCreateDto) } returns FollowDto(
                    id = 1L,
                    followerId = followerMemberDto.id,
                    followeeId = followeeMemberDto.id,
                    createdAt = LocalDateTime.now().withNano(0),
                )

                createFollowMemberUsecase.execute(
                    followerId = followerMemberDto.id,
                    followeeId = followeeMemberDto.id,
                )

                then("이와 관련된 메서드들은 최소 1번씩 호출된다") {
                    verify { mockFollowService.create(dto = followCreateDto) }
                    verify { mockMemberReadService.findMember(id = followerMemberDto.id) }
                    verify { mockMemberReadService.findMember(id = followeeMemberDto.id) }
                }
            }
        }
    },
)

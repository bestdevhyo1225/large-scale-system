package com.hyoseok.follow.entity

import com.hyoseok.follow.entity.Follow.ErrorMessage.SAME_MEMBER_ID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class FollowTests : DescribeSpec(
    {
        describe("invoke 메서드는") {
            it("Follow 엔티티를 생성한다") {
                // given
                val followerId = 1L
                val followeeId = 2L

                // when
                val follow = Follow(followerId = followerId, followeeId = followeeId)

                // then
                follow.followerId.shouldBe(followerId)
                follow.followeeId.shouldBe(followeeId)
            }

            context("followerId 와 followeeId 가 같은 경우") {
                it("예외를 던진다") {
                    // given
                    val followerId = 1L
                    val followeeId = 1L

                    // when
                    val exception: IllegalArgumentException = shouldThrow {
                        Follow(followerId = followerId, followeeId = followeeId)
                    }

                    // then
                    exception.localizedMessage.shouldBe(SAME_MEMBER_ID)
                }
            }
        }
    },
)

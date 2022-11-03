package com.hyoseok.follow

import com.hyoseok.exception.DomainExceptionMessage.FAIL_ADD_FOLLOWEE
import com.hyoseok.exception.DomainExceptionMessage.INVALID_FOLLOWER_ID_FOLLOWEE_ID
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.Follow.Companion.MAX_FOLLOWEE_LIMIT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class FollowTests : DescribeSpec(
    {
        describe("create 메서드는") {
            it("팔로우 엔티티를 생성한다") {
                // given
                val followerId = 1L
                val followeeId = 2L
                val followeeCount = 10L

                // when
                val follow: Follow = Follow.create(
                    followerId = followerId,
                    followeeId = followeeId,
                    followeeCount = followeeCount,
                )

                // then
                follow.followerId.shouldBe(followerId)
                follow.followeeId.shouldBe(followeeId)
            }

            context("자신이 자신을 팔로우 하는 경우") {
                it("예외를 던진다") {
                    // given
                    val followerId = 1L
                    val followeeId = 1L
                    val followeeCount = 10L

                    // when
                    val exception: IllegalArgumentException = shouldThrow {
                        Follow.create(followerId = followerId, followeeId = followeeId, followeeCount = followeeCount)
                    }

                    // then
                    exception.localizedMessage.shouldBe(INVALID_FOLLOWER_ID_FOLLOWEE_ID)
                }
            }

            context("팔로우 할 카운트를 초과하면") {
                it("IllegalArgumentException 예외를 던진다") {
                    // given
                    val followerId = 1L
                    val followeeId = 2L
                    val followeeCount = MAX_FOLLOWEE_LIMIT.plus(1)

                    // when
                    val exception: IllegalArgumentException = shouldThrow {
                        Follow.create(followerId = followerId, followeeId = followeeId, followeeCount = followeeCount)
                    }

                    // then
                    exception.localizedMessage.shouldBe(FAIL_ADD_FOLLOWEE)
                }
            }
        }
    },
)

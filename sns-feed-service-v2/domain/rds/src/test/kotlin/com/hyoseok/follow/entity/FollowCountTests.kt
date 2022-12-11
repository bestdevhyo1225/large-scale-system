package com.hyoseok.follow.entity

import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_TOTAL_FOLLOWEE
import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_TOTAL_FOLLOWER
import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_VALUE
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

internal class FollowCountTests : BehaviorSpec(
    {
        given("FollowCount가 주어질 때") {
            val memberId = 1L
            val followCount = FollowCount(memberId = memberId, totalFollower = 1, totalFollowee = 1)

            `when`("increaseTotalFollower 메서드에 1을 넣으면, totalFollower 값을 증가시키고") {
                followCount.increaseTotalFollower(value = 1)

                then("totalFollower는 2를 반환한다") {
                    followCount.totalFollower.shouldBe(2)
                }
            }

            then("increaseTotalFollower 메서드에 -1 값을 넣으면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.increaseTotalFollower(value = -1)
                }

                exception.localizedMessage.shouldBe(INVALID_VALUE)
            }

            `when`("decreaseTotalFollower 메서드는 1을 넣으면, totalFollower 값을 감소시키고") {
                followCount.decreaseTotalFollower(value = 1)

                then("totalFollower는 1을 반환한다") {
                    followCount.totalFollower.shouldBe(1)
                }
            }

            then("decreaseTotalFollower 메서드에 -1 값을 넣으면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.decreaseTotalFollower(value = -1)
                }

                exception.localizedMessage.shouldBe(INVALID_VALUE)
            }

            then("decreaseTotalFollower 메서드의 결과가 음수이면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.decreaseTotalFollower(value = 100)
                }

                exception.localizedMessage.shouldBe(INVALID_TOTAL_FOLLOWER)
            }

            `when`("increaseTotalFollowee 메서드에 1을 넣으면, totalFollowee 값을 증가시키고") {
                followCount.increaseTotalFollowee(value = 1)

                then("totalFollower는 2를 반환한다") {
                    followCount.totalFollowee.shouldBe(2)
                }
            }

            then("increaseTotalFollowee 메서드에 -1 값을 넣으면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.increaseTotalFollowee(value = -1)
                }

                exception.localizedMessage.shouldBe(INVALID_VALUE)
            }

            `when`("decreaseTotalFollowee 메서드는 1을 넣으면, totalFollowee 값을 감소시키고") {
                followCount.decreaseTotalFollowee(value = 1)

                then("totalFollower는 1을 반환한다") {
                    followCount.totalFollowee.shouldBe(1)
                }
            }

            then("decreaseTotalFollowee 메서드에 -1 값을 넣으면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.decreaseTotalFollowee(value = -1)
                }

                exception.localizedMessage.shouldBe(INVALID_VALUE)
            }

            then("decreaseTotalFollowee 메서드의 결과가 음수이면, 예외가 발생한다") {
                val exception: IllegalArgumentException = shouldThrow {
                    followCount.decreaseTotalFollowee(value = 100)
                }

                exception.localizedMessage.shouldBe(INVALID_TOTAL_FOLLOWEE)
            }
        }
    },
)

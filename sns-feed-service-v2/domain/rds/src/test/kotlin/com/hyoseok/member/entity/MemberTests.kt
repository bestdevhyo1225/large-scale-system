package com.hyoseok.member.entity

import com.hyoseok.member.entity.Member.Companion.NUMBER_OF_INFLUENCER
import com.hyoseok.member.entity.Member.ErrorMessage.MAX_LIMIT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

internal class MemberTests : DescribeSpec(
    {
        describe("invoke 메서드는") {
            it("Member 엔티티를 생성한다") {
                // given
                val name = "JangHyoSeok"

                // when
                val member = Member(name = name)

                // then
                member.name.shouldBe(name)
            }

            context("입력할 수 있는 이름의 길이를 초과하면") {
                it("IllegalArgumentException 예외를 던진다") {
                    // given
                    val name = "JangHyoSeok291u3dzcv12"

                    // when
                    val exception: IllegalArgumentException = shouldThrow { Member(name = name) }

                    // then
                    exception.localizedMessage.shouldBe(MAX_LIMIT)
                }
            }
        }

        describe("switchInfluencerAccount 메서드는") {
            it("인플루언서 계정으로 전환한다") {
                // given
                val name = "JangHyoSeok"
                val member = Member(name = name)
                val followerCount = NUMBER_OF_INFLUENCER

                // when
                member.switchInfluencerAccount(followerCount = followerCount)

                // then
                member.isInfluencer().shouldBeTrue()
            }

            context("인플루언서 기준에 충족하지 못한 경우") {
                it("IllegalArgumentException 예외를 던진다") {
                    // given
                    val name = "JangHyoSeok"
                    val member = Member(name = name)
                    val followerCount = NUMBER_OF_INFLUENCER.minus(other = 1)

                    // when
                    shouldThrow<IllegalArgumentException> {
                        member.switchInfluencerAccount(followerCount = followerCount)
                    }

                    // then
                    member.isInfluencer().shouldBeFalse()
                }
            }
        }

        describe("switchNormalAccount 메서드는") {
            it("일반 계정으로 전환한다") {
                // given
                val name = "JangHyoSeok"
                val member = Member(name = name)
                val followerCount = NUMBER_OF_INFLUENCER.minus(other = 1)

                // when
                member.switchNormalAccount(followerCount = followerCount)

                // then
                member.isInfluencer().shouldBeFalse()
            }

            context("일반 계정 조건에 충족하지 못한 경우") {
                it("IllegalArgumentException 예외를 던진다") {
                    // given
                    val name = "JangHyoSeok"
                    val member = Member(name = name)
                    val followerCount = NUMBER_OF_INFLUENCER

                    member.switchInfluencerAccount(followerCount = followerCount)

                    // when
                    shouldThrow<IllegalArgumentException> {
                        member.switchNormalAccount(followerCount = followerCount)
                    }

                    // then
                    member.isInfluencer().shouldBeTrue()
                }
            }
        }
    },
)

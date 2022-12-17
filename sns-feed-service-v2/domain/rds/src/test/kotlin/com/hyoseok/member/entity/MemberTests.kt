package com.hyoseok.member.entity

import com.hyoseok.member.entity.Member.ErrorMessage.MAX_LIMIT
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
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
    },
)

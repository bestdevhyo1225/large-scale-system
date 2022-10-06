package com.hyoseok.member

import com.hyoseok.member.entity.Member
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
        }
    },
)

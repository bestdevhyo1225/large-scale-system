package com.hyoseok.coupon

import com.hyoseok.coupon.entity.CouponIssued
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class CouponIssuedTests : DescribeSpec(
    {
        describe("invoke 메서드를 통해") {
            it("CouponIssued 엔티티를 생성한다") {
                // given
                val couponId = 1L
                val memberId = 1L

                // when
                val couponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                // then
                couponIssued.couponId.shouldBe(couponId)
                couponIssued.memberId.shouldBe(memberId)
            }
        }
    },
)

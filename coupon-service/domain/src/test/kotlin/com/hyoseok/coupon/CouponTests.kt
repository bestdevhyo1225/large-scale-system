package com.hyoseok.coupon

import com.hyoseok.coupon.entity.Coupon
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class CouponTests : DescribeSpec(
    {
        describe("invoke 메서드를 통해") {
            it("Coupon 엔티티를 생성한다") {
                // given
                val name = "XXX 쿠폰 데이"
                val issuedLimitCount = 5_000
                val now: LocalDateTime = LocalDateTime.now()
                val issuedStartedAt: LocalDateTime = now
                val issuedEndedAt: LocalDateTime = now.plusDays(5)
                val availableStartedAt: LocalDateTime = now
                val availableEndedAt: LocalDateTime = now.plusMonths(1)

                // when
                val coupon = Coupon(
                    name = name,
                    issuedLimitCount = issuedLimitCount,
                    issuedStartedAt = issuedStartedAt,
                    issuedEndedAt = issuedEndedAt,
                    availableStartedAt = availableStartedAt,
                    availableEndedAt = availableEndedAt,
                )

                // then
                coupon.name.shouldBe(name)
                coupon.issuedLimitCount.shouldBe(issuedLimitCount)
                coupon.issuedStartedAt.shouldBe(issuedStartedAt)
                coupon.issuedEndedAt.shouldBe(issuedEndedAt)
                coupon.availableStartedAt.shouldBe(availableStartedAt)
                coupon.availableEndedAt.shouldBe(availableEndedAt)
            }
        }
    },
)

package com.hyoseok.coupon

import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class CouponIssuedFailLogTests : DescribeSpec(
    {
        describe("invoke 메서드를 통해") {
            it("CouponIssuedFailLog 엔티티를 생성한다") {
                // given
                val applicationType = CouponIssuedFailLogApplicationType.PRODUCER
                val data = "data"
                val error = RuntimeException("Occured RuntimeException")

                // when
                val couponIssuedFailLog = CouponIssuedFailLog(
                    applicationType = applicationType,
                    data = data,
                    errorMessage = error.localizedMessage,
                )

                // then
                couponIssuedFailLog.applicationType.shouldBe(applicationType)
                couponIssuedFailLog.data.shouldBe(data)
                couponIssuedFailLog.errorMessage.shouldBe(error.localizedMessage)
            }
        }
    },
)

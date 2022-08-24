package com.bestdev.payment

import com.bestdev.payment.entity.Payment
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.shouldBe

internal class PaymentTests : DescribeSpec(
    {
        describe("operator invoke 메서드는") {
            it("Payment 엔티티를 생성한다.") {
                // given
                val orderId = 1L
                val pgUniqueId = "XSDQKDCV23"

                // when
                val payment = Payment(orderId = orderId, pgUniqueId = pgUniqueId)

                // then
                payment.id.shouldBeZero()
                payment.orderId.shouldBe(orderId)
                payment.pgUniqueId.shouldBe(pgUniqueId)
            }
        }
    },
)

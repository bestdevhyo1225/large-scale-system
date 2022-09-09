package com.hyoseok.order

import com.hyoseok.order.entity.Order
import com.hyoseok.order.entity.OrderItem
import com.hyoseok.order.entity.OrderPayment
import com.hyoseok.order.entity.enums.OrderItemCategory
import com.hyoseok.order.entity.enums.OrderPaymentMethod
import com.hyoseok.order.entity.enums.OrderStatus
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

internal class OrderTests : DescribeSpec(
    {
        describe("invoke 메서드는") {
            it("Order 엔티티를 생성한다.") {
                // given
                val memberId = 1231L
                val orderItems: List<OrderItem> = listOf(
                    OrderItem(itemCategory = "book", itemName = "Spring Guide 2.0"),
                    OrderItem(itemCategory = "clothes", itemName = "신상!!"),
                )
                val orderPayments: List<OrderPayment> = listOf(
                    OrderPayment(paymentMethod = "card", price = 44_700f),
                    OrderPayment(paymentMethod = "point", price = 5_300f),
                )

                // when
                val order = Order(memberId = memberId, orderItems = orderItems, orderPayments = orderPayments)

                // then
                order.id.shouldBeZero()
                order.memberId.shouldBe(memberId)
                order.orderItems.map {
                    it.id.shouldBeZero()
                    it.itemCategory.shouldNotBeNull()
                    it.itemCategory.shouldBeInstanceOf<OrderItemCategory>()
                    it.itemName.shouldNotBeNull()
                    it.createdAt.shouldNotBeNull()
                    it.updatedAt.shouldNotBeNull()
                }
                order.orderPayments.map {
                    it.id.shouldBeZero()
                    it.paymentMethod.shouldNotBeNull()
                    it.paymentMethod.shouldBeInstanceOf<OrderPaymentMethod>()
                    it.price.shouldNotBeNull()
                    it.createdAt.shouldNotBeNull()
                    it.updatedAt.shouldNotBeNull()
                }
                order.status.shouldBe(OrderStatus.WAIT)
                order.orderedAt.shouldNotBeNull()
                order.updatedAt.shouldNotBeNull()
            }
        }
    },
)

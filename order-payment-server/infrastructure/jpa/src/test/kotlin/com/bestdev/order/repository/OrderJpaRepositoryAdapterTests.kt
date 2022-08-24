package com.bestdev.order.repository

import com.bestdev.JpaRepositoryAdapterTestable
import com.bestdev.order.entity.Order
import com.bestdev.order.entity.OrderItem
import com.bestdev.order.entity.OrderPayment
import com.bestdev.order.entity.enums.OrderStatus
import com.bestdev.order.repository.read.OrderReadRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class OrderJpaRepositoryAdapterTests : JpaRepositoryAdapterTestable, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderReadRepository: OrderReadRepository

    init {
        this.describe("save 메서드는") {
            it("Order, OrderItem, OrderPayment 엔티티를 저장한다.") {
                // given
                val orderItems: List<OrderItem> = listOf(
                    OrderItem(itemCategory = "book", itemName = "Spring Guide 2.0"),
                    OrderItem(itemCategory = "clothes", itemName = "신상!!"),
                )
                val orderPayments: List<OrderPayment> = listOf(
                    OrderPayment(paymentMethod = "card", price = 44_700f),
                    OrderPayment(paymentMethod = "point", price = 5_300f),
                )
                val order = Order(memberId = 1823L, orderItems = orderItems, orderPayments = orderPayments)

                // when
                orderRepository.save(order = order)

                // then
                order.id.shouldNotBeZero()
                order.orderItems.forEach { it.id.shouldNotBeZero() }
                order.orderPayments.forEach { it.id.shouldNotBeZero() }

                val findOrder = orderReadRepository.findWithItemsAndPayments(id = order.id)

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
                findOrder.orderItems.shouldNotBeEmpty()
                findOrder.orderPayments.shouldNotBeEmpty()

                val findOrderItems = findOrder.orderItems.sortedBy { it.id }
                val findOrderPayments = findOrder.orderPayments.sortedBy { it.id }

                findOrderItems.forEachIndexed { index, orderItem ->
                    orderItem.id.shouldNotBeZero()
                    orderItem.id.shouldBe(orderItems[index].id)
                    orderItem.shouldBe(orderItems[index])
                }
                findOrderPayments.forEachIndexed { index, orderPayment ->
                    orderPayment.id.shouldNotBeZero()
                    orderPayment.id.shouldBe(orderPayments[index].id)
                    orderPayment.shouldBe(orderPayments[index])
                }
            }
        }

        this.describe("updateStatus 메서드는") {
            it("[1] Order 엔티티의 status를 수정한다.") {
                // given
                val orderItems: List<OrderItem> = listOf(
                    OrderItem(itemCategory = "book", itemName = "Spring Guide 2.0"),
                    OrderItem(itemCategory = "clothes", itemName = "신상!!"),
                )
                val orderPayments: List<OrderPayment> = listOf(
                    OrderPayment(paymentMethod = "card", price = 44_700f),
                    OrderPayment(paymentMethod = "point", price = 5_300f),
                )
                val order = Order(memberId = 1823L, orderItems = orderItems, orderPayments = orderPayments)
                orderRepository.save(order = order)

                // when
                order.changeStatus(status = OrderStatus.COMPLETE)
                orderRepository.updateStatus(order = order)

                // then
                val findOrder = orderRepository.find(id = order.id)

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
            }

            it("[2] Order 엔티티의 status를 수정한다.") {
                // given
                val status = OrderStatus.COMPLETE
                val orderItems: List<OrderItem> = listOf(
                    OrderItem(itemCategory = "book", itemName = "Spring Guide 2.0"),
                    OrderItem(itemCategory = "clothes", itemName = "신상!!"),
                )
                val orderPayments: List<OrderPayment> = listOf(
                    OrderPayment(paymentMethod = "card", price = 44_700f),
                    OrderPayment(paymentMethod = "point", price = 5_300f),
                )
                val order = Order(memberId = 1823L, orderItems = orderItems, orderPayments = orderPayments)
                orderRepository.save(order = order)

                // when
                orderRepository.updateStatus(id = order.id, status = status)

                // then
                val findOrder = orderRepository.find(id = order.id)

                findOrder.shouldNotBeNull()
                findOrder.status.shouldBe(status)
            }
        }
    }
}

package com.bestdev.order.repository.read

import com.bestdev.JpaRepositoryAdapterTestable
import com.bestdev.order.entity.Order
import com.bestdev.order.entity.OrderItem
import com.bestdev.order.entity.OrderPayment
import com.bestdev.order.repository.OrderRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class OrderReadJpaRepositoryAdapterTests : JpaRepositoryAdapterTestable, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderReadRepository: OrderReadRepository

    init {
        this.describe("find 메서드는") {
            it("Order 엔티티만 조회한다.") {
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
                val findOrder = orderReadRepository.find(id = order.id)

                // then
                order.id.shouldNotBeZero()
                order.orderItems.forEach { it.id.shouldNotBeZero() }
                order.orderPayments.forEach { it.id.shouldNotBeZero() }

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
                findOrder.orderItems.shouldBeEmpty()
                findOrder.orderPayments.shouldBeEmpty()
            }
        }

        this.describe("findWithItems 메서드는") {
            it("Order, OrderItem 엔티티를 조회한다.") {
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
                val findOrder = orderReadRepository.findWithItems(id = order.id)

                // then
                order.id.shouldNotBeZero()
                order.orderItems.forEach { it.id.shouldNotBeZero() }
                order.orderPayments.forEach { it.id.shouldNotBeZero() }

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
                findOrder.orderItems.shouldNotBeEmpty()
                findOrder.orderItems.forEachIndexed { index, orderItem ->
                    orderItem.id.shouldNotBeZero()
                    orderItem.id.shouldBe(orderItems[index].id)
                    orderItem.shouldBe(orderItems[index])
                }
                findOrder.orderPayments.shouldBeEmpty()
            }
        }

        this.describe("findWithPayments 메서드는") {
            it("Order, OrderPayment 엔티티를 조회한다.") {
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
                val findOrder = orderReadRepository.findWithPayments(id = order.id)

                // then
                order.id.shouldNotBeZero()
                order.orderItems.forEach { it.id.shouldNotBeZero() }
                order.orderPayments.forEach { it.id.shouldNotBeZero() }

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
                findOrder.orderItems.shouldBeEmpty()
                findOrder.orderPayments.shouldNotBeEmpty()
                findOrder.orderPayments.forEachIndexed { index, orderPayment ->
                    orderPayment.id.shouldNotBeZero()
                    orderPayment.id.shouldBe(orderPayments[index].id)
                    orderPayment.shouldBe(orderPayments[index])
                }
            }
        }

        this.describe("findWithItemsAndPayments 메서드는") {
            it("Order, OrderItem, OrderPayment 엔티티를 조회한다.") {
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
                val findOrder = orderReadRepository.findWithItemsAndPayments(id = order.id)

                // then
                order.id.shouldNotBeZero()
                order.orderItems.forEach { it.id.shouldNotBeZero() }
                order.orderPayments.forEach { it.id.shouldNotBeZero() }

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
                findOrder.orderItems.shouldNotBeEmpty()
                findOrder.orderItems.forEachIndexed { index, orderItem ->
                    orderItem.id.shouldNotBeZero()
                    orderItem.id.shouldBe(orderItems[index].id)
                    orderItem.shouldBe(orderItems[index])
                }
                findOrder.orderPayments.shouldNotBeEmpty()
                findOrder.orderPayments.forEachIndexed { index, orderPayment ->
                    orderPayment.id.shouldNotBeZero()
                    orderPayment.id.shouldBe(orderPayments[index].id)
                    orderPayment.shouldBe(orderPayments[index])
                }
            }
        }
    }
}

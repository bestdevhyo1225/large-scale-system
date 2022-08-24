package com.bestdev.order.repository

import com.bestdev.order.entity.Order
import com.bestdev.order.entity.enums.OrderStatus
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class OrderJpaRepositoryAdapterTests : OrderJpaRepositoryAdapterTestable, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var orderRepository: OrderRepository

    init {
        this.describe("save 메서드는") {
            it("Order 엔티티를 저장한다.") {
                // given
                val order = Order(memberId = 1823L)

                // when
                orderRepository.save(order = order)

                // then
                val findOrder = orderRepository.find(id = order.id)

                findOrder.shouldNotBeNull()
                findOrder.shouldBe(order)
            }
        }

        this.describe("updateStatus 메서드는") {
            it("[1] Order 엔티티의 status를 수정한다.") {
                // given
                val order = Order(memberId = 19273L)
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
                val order = Order(memberId = 1923L)
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

package com.bestdev.shipping.repository

import com.bestdev.JpaRepositoryAdapterTestable
import com.bestdev.shipping.entity.Shipping
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class ShippingJpaRepositoryAdapterTests : JpaRepositoryAdapterTestable, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var shippingRepository: ShippingRepository

    init {
        this.describe("saveAll 메서드는") {
            it("Shipping 엔티티 리스트를 저장한다.") {
                // given
                val orderId = 1L
                val shippings: List<Shipping> = listOf(
                    Shipping(orderId = orderId, orderItemId = 1L),
                    Shipping(orderId = orderId, orderItemId = 2L),
                    Shipping(orderId = orderId, orderItemId = 3L),
                )

                // when
                shippingRepository.saveAll(shippings = shippings)

                // then
                shippings.forEach { it.id.shouldNotBeZero() }

                val findShippings = shippingRepository.findAllByOrderId(orderId = orderId)
                    .sortedBy { it.id }

                findShippings.shouldNotBeEmpty()
                findShippings.forEachIndexed { index, shipping ->
                    shipping.id.shouldBe(shippings[index].id)
                    shipping.shouldBe(shippings[index])
                }
            }
        }
    }
}

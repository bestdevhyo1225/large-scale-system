package com.bestdev.payment.repository

import com.bestdev.payment.entity.Payment
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class PaymentJpaRepositoryAdapterTests : PaymentJpaRepositoryAdapterTestable, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var paymentRepository: PaymentRepository

    init {
        this.describe("save 메서드는") {
            it("Payment 엔티티를 저장한다.") {
                // given
                val payment = Payment(orderId = 1L, pgUniqueId = "XASDFQ12ZC")

                // when
                paymentRepository.save(payment = payment)

                // then
                val findPayment = paymentRepository.find(id = payment.id)

                findPayment.shouldNotBeNull()
                findPayment.shouldBe(payment)
            }
        }
    }
}

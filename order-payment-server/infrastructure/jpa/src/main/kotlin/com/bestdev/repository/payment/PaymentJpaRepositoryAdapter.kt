package com.bestdev.repository.payment

import com.bestdev.entity.payment.PaymentJpaEntity
import com.bestdev.exception.InfrastructureExceptionMessage.NOT_FOUND_PAYMENT
import com.bestdev.payment.entity.Payment
import com.bestdev.payment.repository.PaymentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PaymentJpaRepositoryAdapter(
    private val paymentJpaRepository: PaymentJpaRepository,
) : PaymentRepository {

    override fun save(payment: Payment) {
        val paymentJpaEntity = PaymentJpaEntity(payment = payment)
        paymentJpaRepository.save(paymentJpaEntity)
        payment.changeId(id = paymentJpaEntity.id)
    }

    override fun find(id: Long): Payment = with(receiver = findEntityById(id = id)) { toDomainEntity() }

    private fun findEntityById(id: Long): PaymentJpaEntity =
        paymentJpaRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException(NOT_FOUND_PAYMENT)
}

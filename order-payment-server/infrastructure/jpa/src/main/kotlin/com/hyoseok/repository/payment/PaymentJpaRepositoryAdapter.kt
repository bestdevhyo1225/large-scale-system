package com.hyoseok.repository.payment

import com.hyoseok.entity.payment.PaymentJpaEntity
import com.hyoseok.exception.InfrastructureExceptionMessage.NOT_FOUND_PAYMENT
import com.hyoseok.payment.entity.Payment
import com.hyoseok.payment.repository.PaymentRepository
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
        paymentJpaEntity.mapDomainEntityId(payment = payment)
    }

    override fun find(id: Long): Payment = with(receiver = findEntityById(id = id)) { toDomainEntity() }

    private fun findEntityById(id: Long): PaymentJpaEntity =
        paymentJpaRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException(NOT_FOUND_PAYMENT)
}

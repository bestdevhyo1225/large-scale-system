package com.hyoseok.service.payment

import com.hyoseok.payment.entity.Payment
import com.hyoseok.payment.repository.PaymentRepository
import com.hyoseok.service.dto.CreatePaymentDto
import com.hyoseok.service.dto.CreatePaymentResultDto
import com.hyoseok.service.dto.event.CreatedPaymentEventDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val paymentApplicationEventPublisher: ApplicationEventPublisher,
) {

    fun create(dto: CreatePaymentDto): CreatePaymentResultDto {
        val payment: Payment = dto.toDomainEntity()

        paymentRepository.save(payment = payment)
        paymentApplicationEventPublisher.publishEvent(CreatedPaymentEventDto(payment = payment))

        return CreatePaymentResultDto(payment = payment)
    }
}

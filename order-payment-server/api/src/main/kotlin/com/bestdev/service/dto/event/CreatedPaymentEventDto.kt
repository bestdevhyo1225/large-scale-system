package com.bestdev.service.dto.event

import com.bestdev.payment.entity.Payment

data class CreatedPaymentEventDto(
    val orderId: Long,
) {
    companion object {
        operator fun invoke(payment: Payment) = CreatedPaymentEventDto(orderId = payment.orderId)
    }
}

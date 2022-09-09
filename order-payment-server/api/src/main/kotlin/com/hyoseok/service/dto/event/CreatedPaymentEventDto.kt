package com.hyoseok.service.dto.event

import com.hyoseok.payment.entity.Payment

data class CreatedPaymentEventDto(
    val orderId: Long,
) {
    companion object {
        operator fun invoke(payment: Payment) = CreatedPaymentEventDto(orderId = payment.orderId)
    }
}

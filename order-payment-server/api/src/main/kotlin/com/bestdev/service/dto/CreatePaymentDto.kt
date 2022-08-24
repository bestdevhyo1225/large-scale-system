package com.bestdev.service.dto

import com.bestdev.payment.entity.Payment

data class CreatePaymentDto(
    val merchantUniqueId: Long,
    val pgUniqueId: String,
) {
    fun toDomainEntity() = Payment(orderId = merchantUniqueId, pgUniqueId = pgUniqueId)
}

data class CreatePaymentResultDto(
    val id: Long,
) {
    companion object {
        operator fun invoke(payment: Payment) = CreatePaymentResultDto(id = payment.id)
    }
}

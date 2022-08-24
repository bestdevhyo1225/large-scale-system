package com.bestdev.service.dto.event

import com.bestdev.order.entity.Order

data class CreatedOrderEventDto(
    val orderId: Long,
) {
    companion object {
        operator fun invoke(order: Order) = CreatedOrderEventDto(orderId = order.id)
    }
}

package com.hyoseok.service.dto.event

import com.hyoseok.order.entity.Order

data class CreatedOrderEventDto(
    val orderId: Long,
) {
    companion object {
        operator fun invoke(order: Order) = CreatedOrderEventDto(orderId = order.id)
    }
}

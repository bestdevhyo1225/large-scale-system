package com.bestdev.service.dto

import com.bestdev.order.entity.Order

data class CreateOrderDto(
    val memberId: Long,
) {
    fun toDomainEntity() = Order(memberId = memberId)
}

data class CreateOrderResultDto(
    val id: Long,
) {
    companion object {
        operator fun invoke(order: Order) = with(receiver = order) { CreateOrderResultDto(id = id) }
    }
}

data class CreateSubInfoOfOrderDto(
    val orderId: Long,
)

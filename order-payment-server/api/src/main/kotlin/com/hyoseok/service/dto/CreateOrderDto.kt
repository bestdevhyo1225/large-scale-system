package com.hyoseok.service.dto

import com.hyoseok.order.entity.Order
import com.hyoseok.order.entity.OrderItem
import com.hyoseok.order.entity.OrderPayment

data class CreateOrderDto(
    val memberId: Long,
    val orderItemDtos: List<CreateOrderItemDto>,
    val orderPaymentDtos: List<CreateOrderPaymentDto>,
) {
    fun toDomainEntity() = Order(
        memberId = memberId,
        orderItems = orderItemDtos.map { it.toDomainEntity() },
        orderPayments = orderPaymentDtos.map { it.toDomainEntity() },
    )
}

data class CreateOrderItemDto(
    val itemCategory: String,
    val itemName: String,
) {
    fun toDomainEntity() = OrderItem(itemCategory = itemCategory, itemName = itemName)
}

data class CreateOrderPaymentDto(
    val paymentMethod: String,
    val price: Float,
) {
    fun toDomainEntity() = OrderPayment(paymentMethod = paymentMethod, price = price)
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

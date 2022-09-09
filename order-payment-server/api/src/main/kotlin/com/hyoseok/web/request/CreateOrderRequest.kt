package com.hyoseok.web.request

import com.hyoseok.service.dto.CreateOrderDto
import com.hyoseok.service.dto.CreateOrderItemDto
import com.hyoseok.service.dto.CreateOrderPaymentDto
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class CreateOrderRequest(
    @field:Positive(message = "memberId 값은 0보다 커야 합니다")
    val memberId: Long,

    @field:NotEmpty(message = "orderItems 값을 입력하세요")
    val orderItems: List<@Valid CreateOrderItemRequest>,

    @field:NotEmpty(message = "orderPayments 값을 입력하세요")
    val orderPayments: List<@Valid CreateOrderPaymentRequest>,
) {
    fun toServiceDto() = CreateOrderDto(
        memberId = memberId,
        orderItemDtos = orderItems.map { it.toServiceDto() },
        orderPaymentDtos = orderPayments.map { it.toServiceDto() },
    )
}

data class CreateOrderItemRequest(
    @field:NotBlank(message = "itemCategory 값을 입력하세요")
    val itemCategory: String,

    @field:NotBlank(message = "itemName 값을 입력하세요")
    val itemName: String,
) {
    fun toServiceDto() = CreateOrderItemDto(itemCategory = itemCategory, itemName = itemName)
}

data class CreateOrderPaymentRequest(
    @field:NotBlank(message = "paymentMethod 값을 입력하세요")
    val paymentMethod: String,

    @field:NotBlank(message = "price 값을 입력하세요")
    val price: String,
) {
    fun toServiceDto(): CreateOrderPaymentDto {
        val price = try {
            this.price.toFloat()
        } catch (exception: NumberFormatException) {
            throw IllegalArgumentException("price 값을 실수형으로 입력하세요")
        }
        return CreateOrderPaymentDto(paymentMethod = paymentMethod, price = price)
    }
}

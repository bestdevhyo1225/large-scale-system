package com.bestdev.web.request

import com.bestdev.service.dto.CreateOrderDto
import javax.validation.constraints.Positive

data class CreateOrderRequest(
    @field:Positive(message = "memberId 값은 0보다 커야 합니다")
    val memberId: Long,
) {
    fun toServiceDto() = CreateOrderDto(memberId = memberId)
}

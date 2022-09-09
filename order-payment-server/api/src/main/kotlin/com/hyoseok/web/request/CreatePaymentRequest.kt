package com.hyoseok.web.request

import com.hyoseok.service.dto.CreatePaymentDto
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class CreatePaymentRequest(
    @field:Positive(message = "merchantUniqueId 값은 0보다 커야 합니다")
    val merchantUniqueId: Long,

    @field:NotBlank(message = "pgUniqueId 값을 입력하세요")
    val pgUniqueId: String,
) {
    fun toServiceDto() = CreatePaymentDto(merchantUniqueId = merchantUniqueId, pgUniqueId = pgUniqueId)
}

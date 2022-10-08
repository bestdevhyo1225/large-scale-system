package com.hyoseok.coupon.controller.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyoseok.coupon.service.dto.CouponCreateDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class CouponCreateRequest(
    @field:NotBlank(message = "name을 입력하세요")
    val name: String,

    @field:Positive(message = "totalIssuedQuantity는 0보다 큰 값을 입력하세요")
    val totalIssuedQuantity: Int,

    @field:NotNull(message = "issuedStartedAt를 입력하세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val issuedStartedAt: LocalDateTime,

    @field:NotNull(message = "issuedEndedAt를 입력하세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val issuedEndedAt: LocalDateTime,

    @field:NotNull(message = "availableStartedAt를 입력하세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val availableStartedAt: LocalDateTime,

    @field:NotNull(message = "availableEndedAt를 입력하세요")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val availableEndedAt: LocalDateTime,
) {
    fun toServiceDto() =
        CouponCreateDto(
            name = name,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedStartedAt = issuedStartedAt,
            issuedEndedAt = issuedEndedAt,
            availableStartedAt = availableStartedAt,
            availableEndedAt = availableEndedAt,
        )
}

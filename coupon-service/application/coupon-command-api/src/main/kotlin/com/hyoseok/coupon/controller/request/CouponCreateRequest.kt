package com.hyoseok.coupon.controller.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyoseok.coupon.service.dto.CouponCreateDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class CouponCreateRequest(
    @field:NotBlank(message = "name을 입력하세요")
    @field:Schema(description = "쿠폰 이름", example = "쿠폰", required = true)
    val name: String,

    @field:Positive(message = "totalIssuedQuantity는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "쿠폰 수량", example = "5000", required = true)
    val totalIssuedQuantity: Int,

    @field:NotNull(message = "issuedStartedAt를 입력하세요")
    @field:Schema(description = "쿠폰 발급 시작일", type = "string", example = "2022-10-30 00:00:00", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val issuedStartedAt: LocalDateTime,

    @field:NotNull(message = "issuedEndedAt를 입력하세요")
    @field:Schema(description = "쿠폰 발급 종료일", type = "string", example = "2023-10-29 23:59:59", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val issuedEndedAt: LocalDateTime,

    @field:NotNull(message = "availableStartedAt를 입력하세요")
    @field:Schema(description = "쿠폰 사용 가능 시작일", type = "string", example = "2022-10-30 00:00:00", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val availableStartedAt: LocalDateTime,

    @field:NotNull(message = "availableEndedAt를 입력하세요")
    @field:Schema(description = "쿠폰 사용 가능 종료일", type = "string", example = "2025-10-29 23:59:59", required = true)
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

package com.hyoseok.coupon.controller.request

import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Positive

data class CouponIssuedCreateRequest(
    @field:Positive(message = "memberId는 0보다 큰 값을 입력하세요")
    @field:Schema(description = "회원 고유번호", example = "1", required = true)
    val memberId: Long,
) {
    fun toServiceDto(couponId: Long) = CouponIssuedCreateDto(memberId = memberId, couponId = couponId)
}

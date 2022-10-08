package com.hyoseok.coupon.controller.request

import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import javax.validation.constraints.Positive

data class CouponIssuedCreateRequest(
    @field:Positive(message = "memberId는 0보다 큰 값을 입력하세요")
    val memberId: Long,
) {
    fun toServiceDto(couponId: Long) = CouponIssuedCreateDto(memberId = memberId, couponId = couponId)
}

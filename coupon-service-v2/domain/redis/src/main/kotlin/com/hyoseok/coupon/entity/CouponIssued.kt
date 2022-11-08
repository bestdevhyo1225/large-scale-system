package com.hyoseok.coupon.entity

import java.time.LocalDate

class CouponIssued(
    val couponId: Long,
    val totalIssuedQuantity: Int,
    val issuedDate: LocalDate,
) {

    object ErrorMessage {
        const val SADD_RETURN_NULL = "sadd 명령 수행 후, NULL 값이 반환됨"
        const val SCARD_RETURN_NULL = "scard 명령 수행 후, NULL 값이 반환됨"
    }

    fun getKey() = "coupon:$couponId:issued-date:$issuedDate:issued:members"
}

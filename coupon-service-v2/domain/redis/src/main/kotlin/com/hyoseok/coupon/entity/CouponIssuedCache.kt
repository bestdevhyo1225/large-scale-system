package com.hyoseok.coupon.entity

import java.time.LocalDate

data class CouponIssuedCache(
    val couponId: Long,
    val totalIssuedQuantity: Int,
    val issuedDate: LocalDate,
) {

    val expireTime: Long = 3

    enum class Status(val code: Long, val message: String) {
        FAILED(code = -2, message = "쿠폰 발급 실패"),
        EXIT(code = -1, message = "쿠폰 발급 종료"),
        COMPLETE(code = 0, message = "쿠폰 발급 완료"),
        READY(code = 1, message = "쿠폰 발급 준비"),
        ;
    }

    fun getKey() = "coupon:$couponId:issued-date:$issuedDate:issued:members"
}

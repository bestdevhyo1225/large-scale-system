package com.hyoseok.coupon.entity

enum class CouponIssuedStatus(val code: Long, val message: String) {
    FAILED(code = -2, message = "쿠폰 발급 실패"),
    EXIT(code = -1, message = "쿠폰 발급 종료"),
    COMPLETE(code = 0, message = "쿠폰 발급 완료"),
    READY(code = 1, message = "쿠폰 발급 준비"),
    ;
}

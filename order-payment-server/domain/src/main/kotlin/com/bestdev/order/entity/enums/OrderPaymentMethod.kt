package com.bestdev.order.entity.enums

enum class OrderPaymentMethod(val label: String) {
    CARD("신용카드"),
    KAKAO_PAY("카카오 페이"),
    NAVER_PAY("네이버 페이"),
    COUPON("쿠폰"),
    POINT("포인트"),
}

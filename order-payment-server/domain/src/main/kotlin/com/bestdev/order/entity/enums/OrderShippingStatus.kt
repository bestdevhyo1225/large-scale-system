package com.bestdev.order.entity.enums

enum class OrderShippingStatus(val label: String) {
    SHIPPING_WAIT("배송 대기"),
    SHIPPING("배송 중"),
    SHIPPING_COMPLETE("배송 완료"),
}

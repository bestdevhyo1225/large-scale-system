package com.hyoseok.shipping.entity.enums

enum class ShippingStatus(val label: String) {
    SHIPPING_WAIT("배송 대기"),
    SHIPPING("배송 중"),
    SHIPPING_COMPLETE("배송 완료"),
}

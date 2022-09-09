package com.hyoseok.order.entity.enums

enum class OrderItemCategory(val label: String) {
    BOOK("책"),
    CLOTHES("옷"),
    ;

    companion object {
        operator fun invoke(value: String) = OrderItemCategory.valueOf(value.uppercase())
    }
}

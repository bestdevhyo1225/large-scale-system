package com.bestdev.order.entity.enums

import com.bestdev.order.entity.interfaces.Cancelable
import com.bestdev.order.entity.interfaces.Failable

enum class OrderStatus(val label: String) : Cancelable, Failable {
    WAIT("주문 대기") {
        override fun isCanceled() = false
        override fun isFailed() = false
    },
    COMPLETE("주문 완료") {
        override fun isCanceled() = false
        override fun isFailed() = false
    },
    CANCEL("주문 취소") {
        override fun isCanceled() = true
        override fun isFailed() = false
    },
    REFUND("주문 환불") {
        override fun isCanceled() = false
        override fun isFailed() = false
    },
    FAIL("주문 실패") {
        override fun isCanceled() = false
        override fun isFailed() = true
    },
}

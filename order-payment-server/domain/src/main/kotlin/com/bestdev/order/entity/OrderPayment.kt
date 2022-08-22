package com.bestdev.order.entity

import com.bestdev.order.entity.enums.OrderPaymentMethod
import java.time.LocalDateTime
import java.util.Objects

class OrderPayment private constructor(
    paymentMethod: OrderPaymentMethod,
    price: Float,
    orderId: Long,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = 0
        private set

    var paymentMethod: OrderPaymentMethod = paymentMethod
        private set

    var price: Float = price
        private set

    var orderId: Long = orderId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderPayment(id=$id, paymentMethod=$paymentMethod, price=$price, orderId=$orderId, " +
            "createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderPayment = (other as? OrderPayment) ?: return false
        return this.id == otherOrderPayment.id &&
            this.paymentMethod == otherOrderPayment.paymentMethod &&
            this.orderId == otherOrderPayment.orderId &&
            this.createdAt == otherOrderPayment.createdAt &&
            this.updatedAt == otherOrderPayment.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

package com.hyoseok.order.entity

import com.hyoseok.order.entity.enums.OrderPaymentMethod
import java.time.LocalDateTime
import java.util.Objects

class OrderPayment private constructor(
    id: Long = 0,
    paymentMethod: OrderPaymentMethod,
    price: Float,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var paymentMethod: OrderPaymentMethod = paymentMethod
        private set

    var price: Float = price
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderPayment(id=$id, paymentMethod=$paymentMethod, price=$price, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderPayment = (other as? OrderPayment) ?: return false
        return this.id == otherOrderPayment.id &&
            this.paymentMethod == otherOrderPayment.paymentMethod &&
            this.createdAt == otherOrderPayment.createdAt &&
            this.updatedAt == otherOrderPayment.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(paymentMethod: String, price: Float): OrderPayment {
            val nowDateTime = LocalDateTime.now().withNano(0)
            return OrderPayment(
                paymentMethod = OrderPaymentMethod(value = paymentMethod),
                price = price,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
        }

        operator fun invoke(
            id: Long,
            paymentMethod: OrderPaymentMethod,
            price: Float,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ) = OrderPayment(
            id = id,
            paymentMethod = paymentMethod,
            price = price,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}

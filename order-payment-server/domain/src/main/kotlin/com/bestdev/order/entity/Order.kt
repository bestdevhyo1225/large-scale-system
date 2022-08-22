package com.bestdev.order.entity

import com.bestdev.exception.DomainExceptionMessage
import com.bestdev.order.entity.enums.OrderStatus
import java.time.LocalDateTime
import java.util.Objects

class Order private constructor(
    id: Long = 0,
    status: OrderStatus,
    orderedAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var status: OrderStatus = status
        private set

    var orderedAt: LocalDateTime = orderedAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Order(id=$id, status=$status, orderedAt=$orderedAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrder = (other as? Order) ?: return false
        return this.id == otherOrder.id &&
            this.status == otherOrder.status &&
            this.orderedAt == otherOrder.orderedAt &&
            this.updatedAt == otherOrder.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    fun changeStatus(status: OrderStatus) {
        if (this.status.isFailed() || this.status.isCanceled()) {
            throw IllegalStateException(DomainExceptionMessage.UNCHANGEABLE_ORDER_STATUS)
        }

        this.status = status
        this.updatedAt = LocalDateTime.now().withNano(0)
    }

    companion object {
        operator fun invoke(): Order {
            val nowDateTime = LocalDateTime.now().withNano(0)
            return Order(status = OrderStatus.WAIT, orderedAt = nowDateTime, updatedAt = nowDateTime)
        }

        operator fun invoke(id: Long, status: OrderStatus, orderedAt: LocalDateTime, updatedAt: LocalDateTime) =
            Order(id = id, status = status, orderedAt = orderedAt, updatedAt = updatedAt)
    }
}

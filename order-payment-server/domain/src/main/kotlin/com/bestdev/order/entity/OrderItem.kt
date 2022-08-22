package com.bestdev.order.entity

import java.time.LocalDateTime
import java.util.Objects

class OrderItem private constructor(
    itemName: String,
    orderId: Long,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = 0
        private set

    var itemName: String = itemName
        private set

    var orderId: Long = orderId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderItem(id=$id, itemName=$itemName, orderId=$orderId, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderItem = (other as? OrderItem) ?: return false
        return this.id == otherOrderItem.id &&
            this.itemName == otherOrderItem.itemName &&
            this.orderId == otherOrderItem.orderId &&
            this.createdAt == otherOrderItem.createdAt &&
            this.updatedAt == otherOrderItem.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

package com.bestdev.order.entity

import com.bestdev.order.entity.enums.OrderItemCategory
import java.time.LocalDateTime
import java.util.Objects

class OrderItem private constructor(
    id: Long = 0,
    itemCategory: OrderItemCategory,
    itemName: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var itemCategory: OrderItemCategory = itemCategory
        private set

    var itemName: String = itemName
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderItem(id=$id, itemCategory=$itemCategory, itemName=$itemName, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderItem = (other as? OrderItem) ?: return false
        return this.id == otherOrderItem.id &&
            this.itemName == otherOrderItem.itemName &&
            this.createdAt == otherOrderItem.createdAt &&
            this.updatedAt == otherOrderItem.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(itemCategory: String, itemName: String): OrderItem {
            val nowDateTime = LocalDateTime.now().withNano(0)
            return OrderItem(
                itemCategory = OrderItemCategory(value = itemCategory),
                itemName = itemName,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
        }

        operator fun invoke(
            id: Long,
            itemCategory: OrderItemCategory,
            itemName: String,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ) = OrderItem(
            id = id,
            itemCategory = itemCategory,
            itemName = itemName,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}

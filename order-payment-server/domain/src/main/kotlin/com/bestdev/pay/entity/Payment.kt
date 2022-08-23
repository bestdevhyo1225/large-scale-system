package com.bestdev.pay.entity

import java.time.LocalDateTime
import java.util.Objects

class Payment private constructor(
    id: Long = 0,
    orderId: Long,
    pgUniqueId: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var orderId: Long = orderId
        private set

    var pgUniqueId: String = pgUniqueId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Payment(id=$id, orderId=$orderId, pgUniqueId=$pgUniqueId, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPayment = (other as? Payment) ?: return false
        return this.id == otherPayment.id &&
            this.orderId == otherPayment.orderId &&
            this.pgUniqueId == otherPayment.pgUniqueId &&
            this.createdAt == otherPayment.createdAt &&
            this.updatedAt == otherPayment.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(orderId: Long, pgUniqueId: String): Payment {
            val nowDateTime = LocalDateTime.now().withNano(0)
            return Payment(orderId = orderId, pgUniqueId = pgUniqueId, createdAt = nowDateTime, updatedAt = nowDateTime)
        }
    }
}

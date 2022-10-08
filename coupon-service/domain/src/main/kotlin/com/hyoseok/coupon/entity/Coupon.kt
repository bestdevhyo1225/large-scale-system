package com.hyoseok.coupon.entity

import java.time.LocalDateTime
import java.util.Objects

class Coupon private constructor(
    id: Long = 0,
    name: String,
    totalIssuedQuantity: Int,
    issuedStartedAt: LocalDateTime,
    issuedEndedAt: LocalDateTime,
    availableStartedAt: LocalDateTime,
    availableEndedAt: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    var id: Long = id
        private set

    var name: String = name
        private set

    var totalIssuedQuantity: Int = totalIssuedQuantity
        private set

    var issuedStartedAt: LocalDateTime = issuedStartedAt
        private set

    var issuedEndedAt: LocalDateTime = issuedEndedAt
        private set

    var availableStartedAt: LocalDateTime = availableStartedAt
        private set

    var availableEndedAt: LocalDateTime = availableEndedAt
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Coupon(id=$id, name=$name, totalIssuedQuantity=$totalIssuedQuantity, " +
        "issuedStartedAt=$issuedStartedAt, issuedEndedAt=$issuedEndedAt, availableStartedAt=$availableStartedAt, " +
        "availableEndedAt=$availableEndedAt, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCoupon: Coupon = (other as? Coupon) ?: return false
        return this.id == otherCoupon.id &&
            this.name == otherCoupon.name &&
            this.totalIssuedQuantity == otherCoupon.totalIssuedQuantity &&
            this.issuedStartedAt == otherCoupon.issuedStartedAt &&
            this.issuedEndedAt == otherCoupon.issuedEndedAt &&
            this.availableStartedAt == otherCoupon.availableStartedAt &&
            this.availableEndedAt == otherCoupon.availableEndedAt &&
            this.createdAt == otherCoupon.createdAt &&
            this.updatedAt == otherCoupon.updatedAt &&
            this.deletedAt == otherCoupon.deletedAt
    }

    companion object {
        operator fun invoke(
            name: String,
            totalIssuedQuantity: Int,
            issuedStartedAt: LocalDateTime,
            issuedEndedAt: LocalDateTime,
            availableStartedAt: LocalDateTime,
            availableEndedAt: LocalDateTime,
        ) = Coupon(
            name = name,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedStartedAt = issuedStartedAt,
            issuedEndedAt = issuedEndedAt,
            availableStartedAt = availableStartedAt,
            availableEndedAt = availableEndedAt,
            createdAt = LocalDateTime.now().withNano(0),
            updatedAt = LocalDateTime.now().withNano(0),
        )

        operator fun invoke(
            id: Long,
            name: String,
            totalIssuedQuantity: Int,
            issuedStartedAt: LocalDateTime,
            issuedEndedAt: LocalDateTime,
            availableStartedAt: LocalDateTime,
            availableEndedAt: LocalDateTime,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
        ) = Coupon(
            id = id,
            name = name,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedStartedAt = issuedStartedAt,
            issuedEndedAt = issuedEndedAt,
            availableStartedAt = availableStartedAt,
            availableEndedAt = availableEndedAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

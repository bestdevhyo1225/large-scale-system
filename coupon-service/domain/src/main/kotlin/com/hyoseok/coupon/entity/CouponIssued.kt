package com.hyoseok.coupon.entity

import java.time.LocalDateTime
import java.util.Objects

class CouponIssued private constructor(
    id: Long = 0,
    couponId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    var id: Long = id
        private set

    var couponId: Long = couponId
        private set

    var memberId: Long = memberId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "CouponIssued(id=$id, couponId=$couponId, memberId=$memberId, createdAt=$createdAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCouponIssued: CouponIssued = (other as? CouponIssued) ?: return false
        return this.id == otherCouponIssued.id &&
            this.couponId == otherCouponIssued.couponId &&
            this.memberId == otherCouponIssued.memberId &&
            this.createdAt == otherCouponIssued.createdAt &&
            this.deletedAt == otherCouponIssued.deletedAt
    }

    companion object {
        operator fun invoke(couponId: Long, memberId: Long) =
            CouponIssued(couponId = couponId, memberId = memberId, createdAt = LocalDateTime.now().withNano(0))

        operator fun invoke(
            id: Long,
            couponId: Long,
            memberId: Long,
            createdAt: LocalDateTime,
            deletedAt: LocalDateTime?,
        ) = CouponIssued(
            id = id,
            couponId = couponId,
            memberId = memberId,
            createdAt = createdAt,
            deletedAt = deletedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

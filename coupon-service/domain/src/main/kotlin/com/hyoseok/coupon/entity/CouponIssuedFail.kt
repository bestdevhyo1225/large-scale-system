package com.hyoseok.coupon.entity

import java.time.LocalDateTime
import java.util.Objects

class CouponIssuedFail private constructor(
    id: Long = 0,
    couponId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
    publishedAt: LocalDateTime? = null,
) {

    var id: Long = id
        private set

    var couponId: Long = couponId
        private set

    var memberId: Long = memberId
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var publishedAt: LocalDateTime? = publishedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "CouponIssuedFail(id=$id, couponId=$couponId, memberId=$memberId, " +
            "createdAt=$publishedAt, publishedAt=$publishedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCouponIssuedFail: CouponIssuedFail = (other as? CouponIssuedFail) ?: return false
        return this.id == otherCouponIssuedFail.id &&
            this.couponId == otherCouponIssuedFail.couponId &&
            this.memberId == otherCouponIssuedFail.memberId &&
            this.createdAt == otherCouponIssuedFail.createdAt &&
            this.publishedAt == otherCouponIssuedFail.publishedAt
    }

    companion object {
        operator fun invoke(couponId: Long, memberId: Long) =
            CouponIssuedFail(couponId = couponId, memberId = memberId, createdAt = LocalDateTime.now().withNano(0))

        operator fun invoke(
            id: Long,
            couponId: Long,
            memberId: Long,
            createdAt: LocalDateTime,
            publishedAt: LocalDateTime?,
        ) = CouponIssuedFail(
            id = id,
            couponId = couponId,
            memberId = memberId,
            createdAt = createdAt,
            publishedAt = publishedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

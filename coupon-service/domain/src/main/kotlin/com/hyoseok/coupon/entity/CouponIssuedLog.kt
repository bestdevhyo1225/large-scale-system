package com.hyoseok.coupon.entity

import java.time.LocalDateTime
import java.util.Objects

class CouponIssuedLog private constructor(
    id: Long = 0,
    couponId: Long,
    memberId: Long,
    instanceId: String,
    isSendCompleted: Boolean = false,
    createdAt: LocalDateTime,
    sendCompletedAt: LocalDateTime? = null,
) {

    var id: Long = id
        private set

    var couponId: Long = couponId
        private set

    var memberId: Long = memberId
        private set

    var instanceId: String = instanceId
        private set

    var isSendCompleted: Boolean = isSendCompleted
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "CouponIssuedLog(id=$id, couponId=$couponId, memberId=$memberId, instanceId=$instanceId, " +
            "isSendCompleted=$isSendCompleted, createdAt=$createdAt, sendCompletedAt=$sendCompletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCouponIssuedLog: CouponIssuedLog = (other as? CouponIssuedLog) ?: return false
        return this.id == otherCouponIssuedLog.id &&
            this.couponId == otherCouponIssuedLog.couponId &&
            this.memberId == otherCouponIssuedLog.memberId &&
            this.instanceId == otherCouponIssuedLog.instanceId &&
            this.isSendCompleted == otherCouponIssuedLog.isSendCompleted &&
            this.createdAt == otherCouponIssuedLog.createdAt &&
            this.sendCompletedAt == otherCouponIssuedLog.sendCompletedAt
    }

    companion object {
        operator fun invoke(couponId: Long, memberId: Long, instanceId: String) =
            CouponIssuedLog(
                couponId = couponId,
                memberId = memberId,
                instanceId = instanceId,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            id: Long,
            couponId: Long,
            memberId: Long,
            instanceId: String,
            isSendCompleted: Boolean,
            createdAt: LocalDateTime,
            sendCompletedAt: LocalDateTime?,
        ) = CouponIssuedLog(
            id = id,
            couponId = couponId,
            memberId = memberId,
            instanceId = instanceId,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
            sendCompletedAt = sendCompletedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

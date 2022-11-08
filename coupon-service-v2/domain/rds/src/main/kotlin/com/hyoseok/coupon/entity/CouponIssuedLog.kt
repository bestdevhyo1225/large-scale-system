package com.hyoseok.coupon.entity

import com.hyoseok.config.jpa.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "coupon_issued_log",
    indexes = [
        Index(name = "ix_coupon_id_member_id", columnList = "coupon_id,member_id"),
    ],
)
@DynamicUpdate
class CouponIssuedLog private constructor(
    couponId: Long,
    memberId: Long,
    instanceId: String,
    isSendCompleted: Boolean,
    createdAt: LocalDateTime,
    sendCompletedAt: LocalDateTime? = null,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "coupon_id", nullable = false)
    var couponId: Long = couponId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "instance_id", nullable = false)
    var instanceId: String = instanceId
        protected set

    @Column(name = "is_send_completed", nullable = false)
    var isSendCompleted: Boolean = isSendCompleted
        protected set

    @Column(name = "send_completed_at", columnDefinition = "DATETIME")
    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        protected set

    override fun toString() =
        "CouponIssuedLog(id=$id, couponId=$couponId, memberId=$memberId, instanceId=$instanceId, " +
            "isSendCompleted=$isSendCompleted, createdAt=$createdAt, sendCompletedAt=$sendCompletedAt)"

    companion object {
        operator fun invoke(
            couponId: Long,
            memberId: Long,
            instanceId: String,
            isSendCompleted: Boolean,
            createdAt: LocalDateTime,
        ) = CouponIssuedLog(
            couponId = couponId,
            memberId = memberId,
            instanceId = instanceId,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
        )
    }

    fun changeSendCompleted(isSendCompleted: Boolean) {
        this.isSendCompleted = isSendCompleted
        if (this.isSendCompleted) {
            this.sendCompletedAt = LocalDateTime.now().withNano(0)
        }
    }
}

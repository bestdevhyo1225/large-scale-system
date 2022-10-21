package com.hyoseok.coupon.entity

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
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
class CouponIssuedLogEntity private constructor(
    couponId: Long,
    memberId: Long,
    instanceId: String,
    isSendCompleted: Boolean,
    createdAt: LocalDateTime,
    sendCompletedAt: LocalDateTime?,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

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

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(name = "send_completed_at", columnDefinition = "DATETIME")
    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        protected set

    companion object {
        operator fun invoke(couponIssuedLog: CouponIssuedLog) =
            with(receiver = couponIssuedLog) {
                CouponIssuedLogEntity(
                    couponId = couponId,
                    memberId = memberId,
                    instanceId = instanceId,
                    isSendCompleted = isSendCompleted,
                    createdAt = createdAt,
                    sendCompletedAt = sendCompletedAt,
                )
            }
    }

    fun changeSendCompleted(isSendCompleted: Boolean) {
        this.isSendCompleted = isSendCompleted
        if (this.isSendCompleted) {
            this.sendCompletedAt = LocalDateTime.now().withNano(0)
        }
    }

    fun toDomain() =
        CouponIssuedLog(
            id = id!!,
            couponId = couponId,
            memberId = memberId,
            instanceId = instanceId,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
            sendCompletedAt = sendCompletedAt,
        )
}

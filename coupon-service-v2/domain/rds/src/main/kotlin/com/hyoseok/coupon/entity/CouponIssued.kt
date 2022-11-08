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
    name = "coupon_issued",
    indexes = [
        Index(name = "uk_coupon_id_member_id", columnList = "coupon_id,member_id", unique = true),
    ],
)
@DynamicUpdate
class CouponIssued private constructor(
    couponId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "coupon_id", nullable = false)
    var couponId: Long = couponId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    override fun toString() =
        "CouponIssued(id=$id, couponId=$couponId, memberId=$memberId, createdAt=$createdAt, deletedAt=$deletedAt)"

    companion object {
        operator fun invoke(
            couponId: Long,
            memberId: Long,
            createdAt: LocalDateTime,
        ) = CouponIssued(
            couponId = couponId,
            memberId = memberId,
            createdAt = createdAt,
        )
    }
}

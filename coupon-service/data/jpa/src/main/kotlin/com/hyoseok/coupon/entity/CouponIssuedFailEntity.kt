package com.hyoseok.coupon.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "coupon_issued")
class CouponIssuedFailEntity private constructor(
    couponId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
    publishedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false)
    var couponId: Long = couponId
        protected set

    @Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(columnDefinition = "DATETIME")
    var publishedAt: LocalDateTime? = publishedAt
        protected set

    companion object {
        operator fun invoke(couponIssuedFail: CouponIssuedFail) =
            with(receiver = couponIssuedFail) {
                CouponIssuedFailEntity(
                    couponId = couponId,
                    memberId = memberId,
                    createdAt = createdAt,
                    publishedAt = publishedAt,
                )
            }
    }
}

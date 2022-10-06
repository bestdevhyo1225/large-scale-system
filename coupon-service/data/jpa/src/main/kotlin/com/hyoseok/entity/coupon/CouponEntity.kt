package com.hyoseok.entity.coupon

import com.hyoseok.coupon.entity.Coupon
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "coupon")
class CouponEntity private constructor(
    name: String,
    issuedLimitCount: Int,
    issuedStartedAt: LocalDateTime,
    issuedEndedAt: LocalDateTime,
    availableStartedAt: LocalDateTime,
    availableEndedAt: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var issuedLimitCount: Int = issuedLimitCount
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var issuedStartedAt: LocalDateTime = issuedStartedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var issuedEndedAt: LocalDateTime = issuedEndedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var availableStartedAt: LocalDateTime = availableStartedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var availableEndedAt: LocalDateTime = availableEndedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @Column(columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    companion object {
        operator fun invoke(coupon: Coupon) =
            with(receiver = coupon) {
                CouponEntity(
                    name = name,
                    issuedLimitCount = issuedLimitCount,
                    issuedStartedAt = issuedStartedAt,
                    issuedEndedAt = issuedEndedAt,
                    availableStartedAt = availableStartedAt,
                    availableEndedAt = availableEndedAt,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    deletedAt = deletedAt,
                )
            }
    }
}

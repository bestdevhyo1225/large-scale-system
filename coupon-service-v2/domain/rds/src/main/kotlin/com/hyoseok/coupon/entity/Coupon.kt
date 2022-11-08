package com.hyoseok.coupon.entity

import com.hyoseok.config.jpa.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "coupon")
@DynamicUpdate
class Coupon private constructor(
    name: String,
    totalIssuedQuantity: Int,
    issuedStartedAt: LocalDateTime,
    issuedEndedAt: LocalDateTime,
    availableStartedAt: LocalDateTime,
    availableEndedAt: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) : BaseEntity(createdAt = createdAt) {

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(name = "total_issued_quantity", nullable = false)
    var totalIssuedQuantity: Int = totalIssuedQuantity
        protected set

    @Column(name = "issued_started_at", nullable = false, columnDefinition = "DATETIME")
    var issuedStartedAt: LocalDateTime = issuedStartedAt
        protected set

    @Column(name = "issued_ended_at", nullable = false, columnDefinition = "DATETIME")
    var issuedEndedAt: LocalDateTime = issuedEndedAt
        protected set

    @Column(name = "available_started_at", nullable = false, columnDefinition = "DATETIME")
    var availableStartedAt: LocalDateTime = availableStartedAt
        protected set

    @Column(name = "available_ended_at", nullable = false, columnDefinition = "DATETIME")
    var availableEndedAt: LocalDateTime = availableEndedAt
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    override fun toString(): String = "Coupon(id=$id, name=$name, totalIssuedQuantity=$totalIssuedQuantity, " +
        "issuedStartedAt=$issuedStartedAt, issuedEndedAt=$issuedEndedAt, availableStartedAt=$availableStartedAt, " +
        "availableEndedAt=$availableEndedAt, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    companion object {
        operator fun invoke(
            name: String,
            totalIssuedQuantity: Int,
            issuedStartedAt: LocalDateTime,
            issuedEndedAt: LocalDateTime,
            availableStartedAt: LocalDateTime,
            availableEndedAt: LocalDateTime,
        ): Coupon {
            val nowDateTime: LocalDateTime = LocalDateTime.now().withNano(0)
            return Coupon(
                name = name,
                totalIssuedQuantity = totalIssuedQuantity,
                issuedStartedAt = issuedStartedAt,
                issuedEndedAt = issuedEndedAt,
                availableStartedAt = availableStartedAt,
                availableEndedAt = availableEndedAt,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
        }
    }
}

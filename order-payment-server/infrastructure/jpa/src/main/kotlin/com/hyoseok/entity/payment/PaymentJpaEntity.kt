package com.hyoseok.entity.payment

import com.hyoseok.payment.entity.Payment
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "payments")
@DynamicUpdate
class PaymentJpaEntity private constructor(
    orderId: Long,
    pgUniqueId: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(nullable = false)
    var orderId: Long = orderId
        protected set

    @Column(nullable = false)
    var pgUniqueId: String = pgUniqueId
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "PaymentJpaEntity(id=$id, orderId=$orderId, pgUniqueId=$pgUniqueId, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPaymentJpaEntity = (other as? PaymentJpaEntity) ?: return false
        return this.id == otherPaymentJpaEntity.id
    }

    fun toDomainEntity() =
        Payment(id = id, orderId = orderId, pgUniqueId = pgUniqueId, createdAt = createdAt, updatedAt = updatedAt)

    fun mapDomainEntityId(payment: Payment) {
        payment.changeId(id = id)
    }

    companion object {
        operator fun invoke(payment: Payment) = with(receiver = payment) {
            PaymentJpaEntity(
                orderId = orderId,
                pgUniqueId = pgUniqueId,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}

package com.bestdev.entity.order

import com.bestdev.order.entity.Order
import com.bestdev.order.entity.enums.OrderStatus
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
@Table(name = "orders")
@DynamicUpdate
class OrderJpaEntity private constructor(
    status: OrderStatus,
    orderedAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(nullable = false)
    var status: OrderStatus = status
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var orderedAt: LocalDateTime = orderedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderJpaEntity(id=$id, status=$status, orderedAt=$orderedAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderJpaEntity = (other as? OrderJpaEntity) ?: return false
        return this.id == otherOrderJpaEntity.id
    }

    fun changeStatus(status: OrderStatus) {
        this.status = status
    }

    fun toDomainEntity() =
        with(receiver = this) { Order(id = id, status = status, orderedAt = orderedAt, updatedAt = updatedAt) }

    companion object {
        operator fun invoke(order: Order) =
            with(receiver = order) { OrderJpaEntity(status = status, orderedAt = orderedAt, updatedAt = updatedAt) }
    }
}

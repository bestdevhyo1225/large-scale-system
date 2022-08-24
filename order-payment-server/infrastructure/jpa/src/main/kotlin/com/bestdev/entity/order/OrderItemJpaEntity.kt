package com.bestdev.entity.order

import com.bestdev.order.entity.OrderItem
import com.bestdev.order.entity.enums.OrderItemCategory
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "orders_items")
@DynamicUpdate
class OrderItemJpaEntity private constructor(
    itemCategory: OrderItemCategory,
    itemName: String,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    var orderJpaEntity: OrderJpaEntity? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var itemCategory: OrderItemCategory = itemCategory
        protected set

    @Column(nullable = false)
    var itemName: String = itemName
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderJpaItemEntity(id=$id, itemCategory=$itemCategory, itemName=$itemName, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderItemJpaEntity = (other as? OrderItemJpaEntity) ?: return false
        return this.id == otherOrderItemJpaEntity.id
    }

    fun chagenOrderJpaEntity(orderJpaEntity: OrderJpaEntity) {
        this.orderJpaEntity = orderJpaEntity
    }

    companion object {
        operator fun invoke(orderItem: OrderItem) = with(receiver = orderItem) {
            OrderItemJpaEntity(
                itemCategory = itemCategory,
                itemName = itemName,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}

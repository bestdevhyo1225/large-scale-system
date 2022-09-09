package com.hyoseok.entity.order

import com.hyoseok.order.entity.Order
import com.hyoseok.order.entity.enums.OrderStatus
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "orders")
@DynamicUpdate
class OrderJpaEntity private constructor(
    memberId: Long,
    status: OrderStatus,
    orderedAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = status
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var orderedAt: LocalDateTime = orderedAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @OneToMany(mappedBy = "orderJpaEntity", cascade = [CascadeType.PERSIST])
    var orderItemJpaEntities: MutableList<OrderItemJpaEntity> = mutableListOf()

    @OneToMany(mappedBy = "orderJpaEntity", cascade = [CascadeType.PERSIST])
    var orderPaymentJpaEntities: MutableList<OrderPaymentJpaEntity> = mutableListOf()

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderJpaEntity(id=$id, memberId=$memberId, status=$status, orderedAt=$orderedAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderJpaEntity = (other as? OrderJpaEntity) ?: return false
        return this.id == otherOrderJpaEntity.id
    }

    fun addOrderItemJpaEntity(orderItemJpaEntity: OrderItemJpaEntity) {
        orderItemJpaEntities.add(orderItemJpaEntity)
        orderItemJpaEntity.chagenOrderJpaEntity(orderJpaEntity = this)
    }

    fun addOrderPaymentJpaEntity(orderPaymentJpaEntity: OrderPaymentJpaEntity) {
        orderPaymentJpaEntities.add(orderPaymentJpaEntity)
        orderPaymentJpaEntity.chagenOrderJpaEntity(orderJpaEntity = this)
    }

    fun changeStatus(status: OrderStatus) {
        this.status = status
    }

    fun toDomainEntity() = with(receiver = this) {
        Order(
            id = id,
            memberId = memberId,
            status = status,
            orderedAt = orderedAt,
            updatedAt = updatedAt,
        )
    }

    fun toDomainEntityWithItems() = with(receiver = this) {
        Order(
            id = id,
            memberId = memberId,
            orderItems = orderItemJpaEntities.map { it.toDomainEntity() },
            orderPayments = listOf(),
            status = status,
            orderedAt = orderedAt,
            updatedAt = updatedAt,
        )
    }

    fun toDomainEntityWithPayments() = with(receiver = this) {
        Order(
            id = id,
            memberId = memberId,
            orderItems = listOf(),
            orderPayments = orderPaymentJpaEntities.map { it.toDomainEntity() },
            status = status,
            orderedAt = orderedAt,
            updatedAt = updatedAt,
        )
    }

    fun toDomainEntityWithAll(orderPaymentJpaEntities: List<OrderPaymentJpaEntity>) =
        with(receiver = this) {
            Order(
                id = id,
                memberId = memberId,
                orderItems = orderItemJpaEntities.map { it.toDomainEntity() },
                orderPayments = orderPaymentJpaEntities.map { it.toDomainEntity() },
                status = status,
                orderedAt = orderedAt,
                updatedAt = updatedAt,
            )
        }

    fun mapDomainEntityId(order: Order) {
        order.changeId(id = id)
        order.orderItems.forEachIndexed { index, orderItem ->
            orderItem.changeId(id = orderItemJpaEntities[index].id)
        }
        order.orderPayments.forEachIndexed { index, orderPayment ->
            orderPayment.changeId(id = orderPaymentJpaEntities[index].id)
        }
    }

    companion object {
        operator fun invoke(order: Order) = with(receiver = order) {
            val orderJpaEntity = OrderJpaEntity(
                memberId = memberId,
                status = status,
                orderedAt = orderedAt,
                updatedAt = updatedAt,
            )
            order.orderItems.map {
                orderJpaEntity.addOrderItemJpaEntity(orderItemJpaEntity = OrderItemJpaEntity(orderItem = it))
            }
            order.orderPayments.map {
                orderJpaEntity.addOrderPaymentJpaEntity(
                    orderPaymentJpaEntity = OrderPaymentJpaEntity(orderPayment = it),
                )
            }
            orderJpaEntity
        }
    }
}

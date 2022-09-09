package com.hyoseok.entity.order

import com.hyoseok.order.entity.OrderPayment
import com.hyoseok.order.entity.enums.OrderPaymentMethod
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
@Table(name = "orders_payments")
@DynamicUpdate
class OrderPaymentJpaEntity private constructor(
    paymentMethod: OrderPaymentMethod,
    price: Float,
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
    var paymentMethod: OrderPaymentMethod = paymentMethod
        protected set

    @Column(nullable = false)
    var price: Float = price
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "OrderPaymentJpaEntity(id=$id, paymentMethod=$paymentMethod, price=$price, " +
            "createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderPaymentJpaEntity = (other as? OrderPaymentJpaEntity) ?: return false
        return this.id == otherOrderPaymentJpaEntity.id
    }

    fun chagenOrderJpaEntity(orderJpaEntity: OrderJpaEntity) {
        this.orderJpaEntity = orderJpaEntity
    }

    fun toDomainEntity() = OrderPayment(
        id = id,
        paymentMethod = paymentMethod,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        operator fun invoke(orderPayment: OrderPayment) = with(receiver = orderPayment) {
            OrderPaymentJpaEntity(
                paymentMethod = paymentMethod,
                price = price,
                createdAt = createdAt,
                updatedAt = updatedAt,
            )
        }
    }
}

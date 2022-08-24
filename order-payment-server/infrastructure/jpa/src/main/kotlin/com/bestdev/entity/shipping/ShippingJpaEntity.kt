package com.bestdev.entity.shipping

import com.bestdev.shipping.entity.Shipping
import com.bestdev.shipping.entity.enums.ShippingCompany
import com.bestdev.shipping.entity.enums.ShippingStatus
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import java.util.Objects
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "shippings")
@DynamicUpdate
class ShippingJpaEntity private constructor(
    orderId: Long,
    orderItemId: Long,
    status: ShippingStatus,
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
    var orderItemId: Long = orderItemId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ShippingStatus = status
        protected set

    @Enumerated(EnumType.STRING)
    @Column
    var company: ShippingCompany? = null
        protected set

    @Column(nullable = false)
    var invoice: String = ""
        protected set

    @Column(columnDefinition = "DATETIME")
    var invoiceRegisterAt: LocalDateTime? = null
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "ShippingJpaEntity(id=$id, orderId=$orderId, orderItemId=$orderItemId, " +
            "status=$status, company=$company, invoice=$invoice, " +
            "invoiceRegisterAt=$invoiceRegisterAt, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherShippingJpaEntity = (other as? ShippingJpaEntity) ?: return false
        return this.id == otherShippingJpaEntity.id
    }

    fun toDomainEntity() =
        Shipping(
            id = id,
            orderId = orderId,
            orderItemId = orderItemId,
            status = status,
            company = company,
            invoice = invoice,
            invoiceRegisterAt = invoiceRegisterAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    fun mapDomainEntityId(shipping: Shipping) {
        shipping.changeId(id = id)
    }

    companion object {
        operator fun invoke(shipping: Shipping) =
            with(receiver = shipping) {
                ShippingJpaEntity(
                    orderId = orderId,
                    orderItemId = orderItemId,
                    status = status,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}

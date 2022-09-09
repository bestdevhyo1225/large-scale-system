package com.hyoseok.shipping.entity

import com.hyoseok.shipping.entity.enums.ShippingCompany
import com.hyoseok.shipping.entity.enums.ShippingStatus
import java.time.LocalDateTime
import java.util.Objects

class Shipping private constructor(
    id: Long = 0,
    orderId: Long,
    orderItemId: Long,
    status: ShippingStatus,
    company: ShippingCompany? = null,
    invoice: String = "",
    invoiceRegisterAt: LocalDateTime? = null,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var orderId: Long = orderId
        private set

    var orderItemId: Long = orderItemId
        private set

    var status: ShippingStatus = status
        private set

    var company: ShippingCompany? = company
        private set

    var invoice: String = invoice
        private set

    var invoiceRegisterAt: LocalDateTime? = invoiceRegisterAt
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Shipping(id=$id, orderId=$orderId, orderItemId=$orderItemId, " +
            "status=$status, company=$company, invoice=$invoice, " +
            "invoiceRegisterAt=$invoiceRegisterAt, createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherShipping = (other as? Shipping) ?: return false
        return this.id == otherShipping.id &&
            this.orderId == otherShipping.orderId &&
            this.orderItemId == otherShipping.orderItemId &&
            this.status == otherShipping.status &&
            this.company == otherShipping.company &&
            this.invoice == otherShipping.invoice &&
            this.invoiceRegisterAt == otherShipping.invoiceRegisterAt &&
            this.createdAt == otherShipping.createdAt &&
            this.updatedAt == otherShipping.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    companion object {
        operator fun invoke(orderId: Long, orderItemId: Long): Shipping {
            val nowDateTime = LocalDateTime.now().withNano(0)
            return Shipping(
                orderId = orderId,
                orderItemId = orderItemId,
                status = ShippingStatus.SHIPPING_WAIT,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
        }

        operator fun invoke(
            id: Long,
            orderId: Long,
            orderItemId: Long,
            status: ShippingStatus,
            company: ShippingCompany?,
            invoice: String,
            invoiceRegisterAt: LocalDateTime?,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
        ) = Shipping(
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
    }
}

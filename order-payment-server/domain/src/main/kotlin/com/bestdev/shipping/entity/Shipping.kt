package com.bestdev.shipping.entity

import com.bestdev.shipping.entity.enums.ShippingCompany
import com.bestdev.shipping.entity.enums.ShippingStatus
import java.time.LocalDateTime
import java.util.Objects

class Shipping private constructor(
    status: ShippingStatus,
    company: ShippingCompany,
    invoice: String = "",
    orderId: Long,
    invoiceRegisterAt: LocalDateTime? = null,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = 0
        private set

    var status: ShippingStatus = status
        private set

    var company: ShippingCompany = company
        private set

    var invoice: String = invoice
        private set

    var orderId: Long = orderId
        private set

    var orderItemId: Long = 0
        private set

    var invoiceRegisterAt: LocalDateTime? = invoiceRegisterAt
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Shipping(id=$id, status=$status, company=$company, invoice=$invoice, " +
            "orderId=$orderId, orderItemId=$orderItemId, invoiceRegisterAt=$invoiceRegisterAt, " +
            "createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherShipping = (other as? Shipping) ?: return false
        return this.id == otherShipping.id &&
            this.status == otherShipping.status &&
            this.company == otherShipping.company &&
            this.invoice == otherShipping.invoice &&
            this.orderId == otherShipping.orderId &&
            this.orderItemId == otherShipping.orderItemId &&
            this.invoiceRegisterAt == otherShipping.invoiceRegisterAt &&
            this.createdAt == otherShipping.createdAt &&
            this.updatedAt == otherShipping.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    fun changeOrderItemId(orderItemId: Long) {
        this.orderItemId = orderItemId
    }
}

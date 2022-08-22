package com.bestdev.order.entity

import com.bestdev.order.entity.enums.OrderShippingCompany
import com.bestdev.order.entity.enums.OrderShippingStatus
import java.time.LocalDateTime
import java.util.Objects

class OrderShipping private constructor(
    shippingStatus: OrderShippingStatus,
    shippingCompany: OrderShippingCompany,
    invoice: String = "",
    orderId: Long,
    invoiceRegisterAt: LocalDateTime? = null,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var id: Long = 0
        private set

    var shippingStatus: OrderShippingStatus = shippingStatus
        private set

    var shippingCompany: OrderShippingCompany = shippingCompany
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
        "OrderShipping(id=$id, shippingStatus=$shippingStatus, shippingCompany=$shippingCompany, invoice=$invoice, " +
            "orderId=$orderId, orderItemId=$orderItemId, invoiceRegisterAt=$invoiceRegisterAt, " +
            "createdAt=$createdAt, updatedAt=$updatedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherOrderShipping = (other as? OrderShipping) ?: return false
        return this.id == otherOrderShipping.id &&
            this.shippingStatus == otherOrderShipping.shippingStatus &&
            this.shippingCompany == otherOrderShipping.shippingCompany &&
            this.invoice == otherOrderShipping.invoice &&
            this.orderId == otherOrderShipping.orderId &&
            this.orderItemId == otherOrderShipping.orderItemId &&
            this.invoiceRegisterAt == otherOrderShipping.invoiceRegisterAt &&
            this.createdAt == otherOrderShipping.createdAt &&
            this.updatedAt == otherOrderShipping.updatedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    fun changeOrderItemId(orderItemId: Long) {
        this.orderItemId = orderItemId
    }
}

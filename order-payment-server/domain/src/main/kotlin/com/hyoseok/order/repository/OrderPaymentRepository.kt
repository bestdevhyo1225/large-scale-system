package com.hyoseok.order.repository

import com.hyoseok.order.entity.OrderPayment

interface OrderPaymentRepository {
    fun saveAll(orderPayments: List<OrderPayment>)
}

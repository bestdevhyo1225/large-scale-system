package com.bestdev.order.repository

import com.bestdev.order.entity.OrderPayment

interface OrderPaymentRepository {
    fun saveAll(orderPayments: List<OrderPayment>)
}

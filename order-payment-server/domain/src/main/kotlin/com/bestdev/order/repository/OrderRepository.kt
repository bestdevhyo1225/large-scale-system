package com.bestdev.order.repository

import com.bestdev.order.entity.Order

interface OrderRepository {
    fun save(order: Order)
    fun updateStatus(order: Order)
    fun find(id: Long): Order
}

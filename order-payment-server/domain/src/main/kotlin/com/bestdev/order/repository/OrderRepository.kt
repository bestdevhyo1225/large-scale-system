package com.bestdev.order.repository

import com.bestdev.order.entity.Order
import com.bestdev.order.entity.enums.OrderStatus

interface OrderRepository {
    fun save(order: Order)
    fun updateStatus(order: Order)
    fun updateStatus(id: Long, status: OrderStatus)
    fun find(id: Long): Order
}

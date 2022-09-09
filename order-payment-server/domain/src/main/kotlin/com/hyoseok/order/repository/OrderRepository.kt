package com.hyoseok.order.repository

import com.hyoseok.order.entity.Order
import com.hyoseok.order.entity.enums.OrderStatus

interface OrderRepository {
    fun save(order: Order)
    fun updateStatus(order: Order)
    fun updateStatus(id: Long, status: OrderStatus)
    fun find(id: Long): Order
}

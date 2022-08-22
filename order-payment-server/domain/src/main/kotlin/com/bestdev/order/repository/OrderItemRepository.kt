package com.bestdev.order.repository

import com.bestdev.order.entity.OrderItem

interface OrderItemRepository {
    fun saveAll(orderItems: List<OrderItem>)
}

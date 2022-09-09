package com.hyoseok.order.repository

import com.hyoseok.order.entity.OrderItem

interface OrderItemRepository {
    fun saveAll(orderItems: List<OrderItem>)
}

package com.bestdev.order.repository.read

import com.bestdev.order.entity.Order

interface OrderReadRepository {
    fun find(id: Long): Order
    fun findWithItems(id: Long): Order
    fun findWithPayments(id: Long): Order
    fun findWithItemsAndPayments(id: Long): Order
}

package com.bestdev.order.repository.read

import com.bestdev.order.entity.Order

interface OrderReadRepository {
    fun findById(id: Long): Order
}

package com.bestdev.order.repository

import com.bestdev.order.entity.OrderShipping

interface OrderShippingRepository {
    fun saveAll(orderShippings: List<OrderShipping>)
}

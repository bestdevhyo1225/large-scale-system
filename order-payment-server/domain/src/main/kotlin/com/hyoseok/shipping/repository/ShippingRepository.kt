package com.hyoseok.shipping.repository

import com.hyoseok.shipping.entity.Shipping

interface ShippingRepository {
    fun save(shipping: Shipping)
    fun saveAll(shippings: List<Shipping>)
    fun findAllByOrderId(orderId: Long): List<Shipping>
}

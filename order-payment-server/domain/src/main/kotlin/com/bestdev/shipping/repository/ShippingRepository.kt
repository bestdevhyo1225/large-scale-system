package com.bestdev.shipping.repository

import com.bestdev.shipping.entity.Shipping

interface ShippingRepository {
    fun save(shipping: Shipping)
    fun saveAll(shippings: List<Shipping>)
    fun findAllByOrderId(orderId: Long): List<Shipping>
}

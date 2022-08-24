package com.bestdev.shipping.repository

import com.bestdev.shipping.entity.Shipping

interface ShippingRepository {
    fun saveAll(shippings: List<Shipping>)
}

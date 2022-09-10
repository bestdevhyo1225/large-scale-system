package com.hyoseok.product.repository

import com.hyoseok.product.entity.ExternalProduct

interface ExternalProductRepository {
    fun save(externalProduct: ExternalProduct)
    fun saveAll(externalProducts: List<ExternalProduct>)
}

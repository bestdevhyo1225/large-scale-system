package com.hyoseok.product.repository.read

import com.hyoseok.product.entity.ExternalProduct

interface ExternalProductReadRepository {
    fun findById(id: Long): ExternalProduct
    fun findByProductId(productId: Long): ExternalProduct
    fun findAllByProductIds(productIds: List<Long>): List<ExternalProduct>
}

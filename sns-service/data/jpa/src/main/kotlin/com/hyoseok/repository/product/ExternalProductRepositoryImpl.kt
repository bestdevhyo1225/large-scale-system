package com.hyoseok.repository.product

import com.hyoseok.entity.product.ExternalProductJpaEntity
import com.hyoseok.product.entity.ExternalProduct
import com.hyoseok.product.repository.ExternalProductRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ExternalProductRepositoryImpl(
    private val externalProductJpaRepository: ExternalProductJpaRepository,
) : ExternalProductRepository {

    override fun save(externalProduct: ExternalProduct) {
        val externalProductJpaEntity = ExternalProductJpaEntity(externalProduct = externalProduct)
        externalProductJpaRepository.save(externalProductJpaEntity)
        externalProductJpaEntity.mapDomainEntity(externalProduct = externalProduct)
    }

    override fun saveAll(externalProducts: List<ExternalProduct>) {
        externalProducts.forEach { save(externalProduct = it) }
    }
}

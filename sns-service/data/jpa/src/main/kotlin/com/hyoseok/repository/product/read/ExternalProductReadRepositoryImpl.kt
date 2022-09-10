package com.hyoseok.repository.product.read

import com.hyoseok.entity.product.QExternalProductJpaEntity.externalProductJpaEntity
import com.hyoseok.exception.Message.NOT_FOUND_PRODUCT
import com.hyoseok.product.entity.ExternalProduct
import com.hyoseok.product.repository.read.ExternalProductReadRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ExternalProductReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : ExternalProductReadRepository {

    override fun findById(id: Long): ExternalProduct {
        val externalProductJpaEntity = jpaQueryFactory
            .selectFrom(externalProductJpaEntity)
            .where(externalProductJpaEntityIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_PRODUCT)

        return externalProductJpaEntity.toDomainEntity()
    }

    override fun findByProductId(productId: Long): ExternalProduct {
        val externalProductJpaEntity = jpaQueryFactory
            .selectFrom(externalProductJpaEntity)
            .where(externalProductJpaEntityProductIdEq(productId = productId))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_PRODUCT)

        return externalProductJpaEntity.toDomainEntity()
    }

    override fun findAllByProductIds(productIds: List<Long>): List<ExternalProduct> {
        return jpaQueryFactory
            .selectFrom(externalProductJpaEntity)
            .where(externalProductJpaEntityProductIdsIn(productIds = productIds))
            .fetch()
            .map { it.toDomainEntity() }
    }

    private fun externalProductJpaEntityIdEq(id: Long) =
        externalProductJpaEntity.id.eq(id)

    private fun externalProductJpaEntityProductIdEq(productId: Long) = externalProductJpaEntity.productId.eq(productId)

    private fun externalProductJpaEntityProductIdsIn(productIds: List<Long>) =
        externalProductJpaEntity.productId.`in`(productIds)
}

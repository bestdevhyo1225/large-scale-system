package com.bestdev.repository.order.read

import com.bestdev.entity.order.OrderJpaEntity
import com.bestdev.entity.order.OrderPaymentJpaEntity
import com.bestdev.entity.order.QOrderItemJpaEntity.orderItemJpaEntity
import com.bestdev.entity.order.QOrderJpaEntity.orderJpaEntity
import com.bestdev.entity.order.QOrderPaymentJpaEntity.orderPaymentJpaEntity
import com.bestdev.exception.InfrastructureExceptionMessage.NOT_FOUND_ORDER
import com.bestdev.order.entity.Order
import com.bestdev.order.repository.read.OrderReadRepository
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class OrderReadJpaRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
) : OrderReadRepository {

    override fun find(id: Long): Order {
        val orderJpaEntity: OrderJpaEntity = jpaQueryFactory
            .selectFrom(orderJpaEntity)
            .where(orderJpaEntityIdEq(id = id))
            .fetchOne()
            ?: throw NoSuchElementException(NOT_FOUND_ORDER)

        return orderJpaEntity.toDomainEntity()
    }

    override fun findWithItems(id: Long): Order {
        val orderJpaEntity: OrderJpaEntity = jpaQueryFactory
            .selectFrom(orderJpaEntity)
            .innerJoin(orderJpaEntity.orderItemJpaEntities, orderItemJpaEntity).fetchJoin()
            .where(orderJpaEntityIdEq(id = id))
            .fetchOne()
            ?: throw NoSuchElementException(NOT_FOUND_ORDER)

        return orderJpaEntity.toDomainEntityWithItems()
    }

    override fun findWithPayments(id: Long): Order {
        val orderJpaEntity: OrderJpaEntity = jpaQueryFactory
            .selectFrom(orderJpaEntity)
            .innerJoin(orderJpaEntity.orderPaymentJpaEntities, orderPaymentJpaEntity).fetchJoin()
            .where(orderJpaEntityIdEq(id = id))
            .fetchOne()
            ?: throw NoSuchElementException(NOT_FOUND_ORDER)

        return orderJpaEntity.toDomainEntityWithPayments()
    }

    override fun findWithItemsAndPayments(id: Long): Order {
        val orderJpaEntity: OrderJpaEntity = jpaQueryFactory
            .selectFrom(orderJpaEntity)
            .innerJoin(orderJpaEntity.orderItemJpaEntities, orderItemJpaEntity).fetchJoin()
            .where(orderJpaEntityIdEq(id = id))
            .fetchOne()
            ?: throw NoSuchElementException(NOT_FOUND_ORDER)

        val orderPaymentJpaEntities: List<OrderPaymentJpaEntity> = jpaQueryFactory
            .selectFrom(orderPaymentJpaEntity)
            .where(orderJpaEntityIdEq(id = id))
            .fetch()

        return orderJpaEntity.toDomainEntityWithAll(orderPaymentJpaEntities = orderPaymentJpaEntities)
    }

    private fun orderJpaEntityIdEq(id: Long): BooleanExpression = orderJpaEntity.id.eq(id)
}

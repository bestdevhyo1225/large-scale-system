package com.bestdev.repository.order

import com.bestdev.entity.order.OrderJpaEntity
import com.bestdev.exception.InfrastructureExceptionMessage
import com.bestdev.order.entity.Order
import com.bestdev.order.repository.OrderRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class OrderRepositoryAdapter(
    private val orderJpaRepository: OrderJpaRepository,
) : OrderRepository {

    private val logger = KotlinLogging.logger {}

    override fun save(order: Order) {
        val orderJpaEntity = OrderJpaEntity(order = order)
        orderJpaRepository.save(orderJpaEntity)
        order.changeId(orderJpaEntity.id)
    }

    override fun updateStatus(order: Order) {
        if (orderJpaRepository.updateStatus(status = order.status, id = order.id) <= 0) {
            logger.error { InfrastructureExceptionMessage.FAIL_CHANGE_ORDER_STATUS }
        }
    }

    override fun find(id: Long): Order =
        with(receiver = findById(id = id)) { toDomainEntity() }

    private fun findById(id: Long): OrderJpaEntity = orderJpaRepository.findByIdOrNull(id = id)
        ?: throw NoSuchElementException(InfrastructureExceptionMessage.NOT_FOUND_ORDER)
}

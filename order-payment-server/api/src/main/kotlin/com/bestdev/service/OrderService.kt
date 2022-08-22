package com.bestdev.service

import com.bestdev.order.entity.Order
import com.bestdev.order.entity.enums.OrderStatus
import com.bestdev.order.repository.OrderRepository
import com.bestdev.service.dto.CreateOrderDto
import com.bestdev.service.dto.CreateOrderResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
) {

    fun create(dto: CreateOrderDto): CreateOrderResultDto {
        val order: Order = dto.toDomainEntity()

        orderRepository.save(order = order)

        return CreateOrderResultDto(order = order)
    }

    fun updateStatus(id: Long, status: String) {
        val order: Order = orderRepository.find(id = id)

        order.changeStatus(status = OrderStatus(value = status))

        orderRepository.updateStatus(order = order)
    }
}

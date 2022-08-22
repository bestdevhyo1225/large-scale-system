package com.bestdev.repository.order

import com.bestdev.entity.order.OrderJpaEntity
import com.bestdev.order.entity.enums.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, Long> {

    @Query("UPDATE OrderJpaEntity o SET o.status = :status, o.updatedAt = :updatedAt WHERE o.id = :id")
    @Modifying(clearAutomatically = true)
    fun updateStatus(status: OrderStatus, updatedAt: LocalDateTime, id: Long): Int
}

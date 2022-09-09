package com.hyoseok.repository.shipping

import com.hyoseok.entity.shipping.ShippingJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ShippingJpaRepository : JpaRepository<ShippingJpaEntity, Long> {

    @Query("SELECT s FROM ShippingJpaEntity s WHERE s.orderId = :orderId")
    fun findAllByOrderId(orderId: Long): List<ShippingJpaEntity>
}

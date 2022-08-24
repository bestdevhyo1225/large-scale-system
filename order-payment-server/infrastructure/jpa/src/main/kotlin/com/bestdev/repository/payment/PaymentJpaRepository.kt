package com.bestdev.repository.payment

import com.bestdev.entity.payment.PaymentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentJpaEntity, Long>

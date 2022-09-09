package com.hyoseok.repository.payment

import com.hyoseok.entity.payment.PaymentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<PaymentJpaEntity, Long>

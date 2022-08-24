package com.bestdev.payment.repository

import com.bestdev.payment.entity.Payment

interface PaymentRepository {
    fun save(payment: Payment)
    fun find(id: Long): Payment
}

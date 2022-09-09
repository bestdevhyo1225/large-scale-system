package com.hyoseok.payment.repository

import com.hyoseok.payment.entity.Payment

interface PaymentRepository {
    fun save(payment: Payment)
    fun find(id: Long): Payment
}

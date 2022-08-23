package com.bestdev.pay.repository

import com.bestdev.pay.entity.Payment

interface PaymentRepository {
    fun save(payment: Payment)
}

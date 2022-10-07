package com.hyoseok.coupon.repository

interface CouponRedisRepository {
    fun <T : Any> sadd(key: String, value: T): Long
    fun scard(key: String): Long
    fun <T : Any> executeUsingTransaction(func: () -> T): T?
}

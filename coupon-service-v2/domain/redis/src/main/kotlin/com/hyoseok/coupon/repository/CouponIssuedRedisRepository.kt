package com.hyoseok.coupon.repository

interface CouponIssuedRedisRepository {
    fun <T : Any> sadd(key: String, value: T): Long
    fun scard(key: String): Long
}

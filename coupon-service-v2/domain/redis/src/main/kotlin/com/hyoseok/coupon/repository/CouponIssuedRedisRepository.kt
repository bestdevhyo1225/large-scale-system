package com.hyoseok.coupon.repository

import java.util.concurrent.TimeUnit

interface CouponIssuedRedisRepository {
    fun expire(key: String, timeout: Long, timeUnit: TimeUnit): Boolean
    fun <T : Any> sadd(key: String, value: T): Long
    fun scard(key: String): Long
}

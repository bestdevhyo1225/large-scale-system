package com.hyoseok.wish.repository

import java.util.concurrent.TimeUnit

interface WishRedisRepository {
    fun <T : Any> sadd(key: String, value: T): Long
    fun scard(key: String): Long?
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun <T : Any> zaddAndExpire(key: String, value: T, score: Double, expireTime: Long, timeUnit: TimeUnit)
    fun zcard(key: String): Long?
    fun <T : Any> zrevRangeByScore(
        key: String,
        minScore: Double,
        maxScore: Double,
        start: Long,
        end: Long,
        clazz: Class<T>,
    ): List<T>
}

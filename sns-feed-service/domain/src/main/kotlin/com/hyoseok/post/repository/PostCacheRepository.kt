package com.hyoseok.post.repository

import java.util.concurrent.TimeUnit

interface PostCacheRepository {
    fun increment(key: String): Long
    fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit)
    fun <T : Any> setAllUsePipeline(keysAndValues: Map<String, T>, expireTime: Long, timeUnit: TimeUnit)
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)
}

package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache
import java.util.concurrent.TimeUnit

interface PostRedisRepository {
    fun <T> get(key: String, clazz: Class<T>): T?
    fun increment(key: String): Long
    fun mget(keys: List<String>): List<PostCache>
    fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit)
    fun setAllUsePipeline(keysAndValues: Map<String, PostCache>, expireTime: Long, timeUnit: TimeUnit)
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zcard(key: String): Long
    fun zremRangeByRank(key: String, start: Long, end: Long)
    fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}
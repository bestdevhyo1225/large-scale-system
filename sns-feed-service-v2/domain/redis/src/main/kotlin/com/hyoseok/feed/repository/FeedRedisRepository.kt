package com.hyoseok.feed.repository

interface FeedRedisRepository {
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)
    fun <T> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}

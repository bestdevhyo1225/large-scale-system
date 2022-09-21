package com.hyoseok.feed.repository

interface FeedCacheRepository {
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)
}

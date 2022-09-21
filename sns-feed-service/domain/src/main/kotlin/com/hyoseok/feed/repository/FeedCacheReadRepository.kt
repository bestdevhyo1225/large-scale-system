package com.hyoseok.feed.repository

interface FeedCacheReadRepository {
    fun <T> zrevrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}

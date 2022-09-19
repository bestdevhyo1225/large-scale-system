package com.hyoseok.post.repository

import java.util.concurrent.TimeUnit

interface PostCacheRepository {
    fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit)
    fun increment(key: String): Long
}

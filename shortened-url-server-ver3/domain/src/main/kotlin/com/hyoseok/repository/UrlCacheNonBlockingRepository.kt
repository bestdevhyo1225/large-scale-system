package com.hyoseok.repository

import java.time.Duration

interface UrlCacheNonBlockingRepository {
    suspend fun <T : Any> set(key: String, value: T, expireDuration: Duration)
    suspend fun <T : Any> get(key: String, clazz: Class<T>): T?
}

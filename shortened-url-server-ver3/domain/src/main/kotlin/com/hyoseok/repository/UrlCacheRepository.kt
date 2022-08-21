package com.hyoseok.repository

import java.util.concurrent.TimeUnit

interface UrlCacheRepository {
    fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit)
    fun <T : Any> get(key: String, clazz: Class<T>): T?
}

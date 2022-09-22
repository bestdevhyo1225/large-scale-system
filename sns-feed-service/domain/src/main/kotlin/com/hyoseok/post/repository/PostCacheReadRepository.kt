package com.hyoseok.post.repository

interface PostCacheReadRepository {
    fun <T> get(key: String, clazz: Class<T>): T?

    fun <T> mget(keys: List<String>, clazz: Class<T>): List<T>
    fun <T> zrevrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T>
}

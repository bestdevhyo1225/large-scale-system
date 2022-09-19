package com.hyoseok.post.repository

interface PostCacheReadRepository {
    fun <T> get(key: String, clazz: Class<T>): T?
}

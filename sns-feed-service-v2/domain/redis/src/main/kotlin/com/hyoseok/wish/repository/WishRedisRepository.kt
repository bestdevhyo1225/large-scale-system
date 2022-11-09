package com.hyoseok.wish.repository

interface WishRedisRepository {
    fun <T : Any> sadd(key: String, value: T): Long
    fun scard(key: String): Long
}

package com.hyoseok.config

object RedissonKeys {
    fun getShortUrlKey(shortUrl: String) = "short-url:$shortUrl"
}

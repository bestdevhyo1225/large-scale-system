package com.hyoseok.config.redis

object RedisKeys {
    fun getUrlsKey(shortUrl: String) = "urls:$shortUrl"
}

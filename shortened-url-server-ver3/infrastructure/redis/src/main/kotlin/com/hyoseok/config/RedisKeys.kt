package com.hyoseok.config

object RedisKeys {
    fun getUrlsKey(shortUrl: String) = "urls:$shortUrl"
}

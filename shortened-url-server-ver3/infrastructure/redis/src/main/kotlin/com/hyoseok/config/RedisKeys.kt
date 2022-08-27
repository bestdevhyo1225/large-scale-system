package com.hyoseok.config

object RedisKeys {
    fun getUrlKey(encodedUrl: String) = "url:$encodedUrl"
}

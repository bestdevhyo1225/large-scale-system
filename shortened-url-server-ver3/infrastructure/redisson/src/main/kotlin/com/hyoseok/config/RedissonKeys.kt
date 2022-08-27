package com.hyoseok.config

object RedissonKeys {
    fun getUrlKey(value: String) = "url:$value"
}

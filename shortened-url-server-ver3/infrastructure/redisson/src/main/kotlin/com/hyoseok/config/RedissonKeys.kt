package com.hyoseok.config

object RedissonKeys {
    fun getLockUrlKey(value: String) = "lock-url:$value"
}

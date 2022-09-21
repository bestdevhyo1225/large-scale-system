package com.hyoseok.config

object RedisKeys {
    fun getMemberFeedKey(id: Long) = "member:$id:feeds"
}

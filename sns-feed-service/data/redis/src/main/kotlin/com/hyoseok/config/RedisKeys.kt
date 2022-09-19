package com.hyoseok.config

object RedisKeys {
    const val POST_ZSET_KEY = "post:keys"

    fun getPostKey(id: Long) = "post:$id"
    fun getPostViewsKey(id: Long) = "post:$id:views"
}

package com.hyoseok.config

object RedisKeys {
    const val POST_KEYS = "post:keys"
    fun getMemberFeedKey(id: Long) = "member:$id:feeds"
    fun getPostKey(id: Long) = "post:$id"
    fun getPostViewsKey(id: Long) = "post:$id:views"
}

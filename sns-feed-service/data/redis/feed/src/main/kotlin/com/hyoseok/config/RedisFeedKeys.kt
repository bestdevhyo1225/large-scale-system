package com.hyoseok.config

object RedisFeedKeys {
    fun getMemberFeedKey(id: Long) = "member:$id:feeds"
}

package com.hyoseok.config

object RedisExpireTimes {
    const val POST: Long = 60 * 60 * 3 // 3시간
    const val POST_VIEWS: Long = 60 * 60 * 3 // 3시간
}

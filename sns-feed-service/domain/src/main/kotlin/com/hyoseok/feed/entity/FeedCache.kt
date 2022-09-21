package com.hyoseok.feed.entity

import java.time.LocalDateTime

data class FeedCache(
    val postId: Long,
    val createdAt: LocalDateTime,
)

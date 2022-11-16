package com.hyoseok.wish.dto

import java.time.LocalDateTime

data class WishEventLogDto(
    val id: Long,
    val postId: Long,
    val memberId: Long,
    val isProcessed: Boolean,
    val publishedAt: LocalDateTime,
    val processedAt: LocalDateTime?,
)

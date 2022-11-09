package com.hyoseok.wish.dto

import java.time.LocalDateTime

data class WishDto(
    val id: Long,
    val postId: Long,
    val memberId: Long,
    val createdAt: LocalDateTime,
)

package com.hyoseok.listener.dto

import java.time.LocalDateTime

data class FollowerSendEventDto(
    val postId: Long,
    val createdAt: LocalDateTime,
    val memberId: Long,
)

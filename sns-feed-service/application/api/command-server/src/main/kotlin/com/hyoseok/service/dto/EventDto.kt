package com.hyoseok.service.dto

import java.time.LocalDateTime

data class FollowerSendEventDto(
    val postId: Long,
    val createdAt: LocalDateTime,
    val memberId: Long,
) {
    companion object {
        operator fun invoke(postId: Long, createdAt: LocalDateTime, followerId: Long) =
            FollowerSendEventDto(postId = postId, createdAt = createdAt, memberId = followerId)
    }
}

data class FolloweeSendEventDto(
    val postId: Long,
    val memberId: Long,
)

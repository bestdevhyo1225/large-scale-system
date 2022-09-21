package com.hyoseok.service.dto

import com.hyoseok.post.entity.Post
import java.time.LocalDateTime

data class FollowerSendEventDto(
    val postId: Long,
    val createdAt: LocalDateTime,
    val memberId: Long,
) {
    companion object {
        operator fun invoke(post: Post, followerId: Long) =
            FollowerSendEventDto(postId = post.id!!, createdAt = post.createdAt, memberId = followerId)
    }
}

data class FolloweeSendEventDto(
    val postId: Long,
    val memberId: Long,
)

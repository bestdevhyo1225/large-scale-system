package com.hyoseok.service.dto

data class FollowerSendEventDto(
    val postId: Long,
    val memberId: Long,
) {
    companion object {
        operator fun invoke(postId: Long, followerId: Long) =
            FollowerSendEventDto(postId = postId, memberId = followerId)
    }
}

data class FolloweeSendEventDto(
    val postId: Long,
    val memberId: Long,
)

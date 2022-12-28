package com.hyoseok.follow.dto

import com.hyoseok.follow.entity.Follow
import java.time.LocalDateTime

data class FollowDto(
    val id: Long,
    val followerId: Long,
    val followeeId: Long,
    val createdAt: LocalDateTime,
) {

    companion object {
        operator fun invoke(follow: Follow): FollowDto =
            with(receiver = follow) {
                FollowDto(id = id!!, followerId = followerId, followeeId = followeeId, createdAt = createdAt)
            }
    }
}

data class FollowCreateDto(
    val followerId: Long,
    val followeeId: Long,
)

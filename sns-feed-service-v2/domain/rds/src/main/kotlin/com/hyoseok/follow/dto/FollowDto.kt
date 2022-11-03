package com.hyoseok.follow.dto

import java.time.LocalDateTime

data class FollowDto(
    val id: Long,
    val followerId: Long,
    val followeeId: Long,
    val createdAt: LocalDateTime,
)

data class FollowCreateDto(
    val followerId: Long,
    val followeeId: Long,
)

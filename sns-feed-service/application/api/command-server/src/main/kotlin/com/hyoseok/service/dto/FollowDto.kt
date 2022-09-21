package com.hyoseok.service.dto

import com.hyoseok.follow.entity.Follow

data class FollowCreateDto(
    val followerId: Long,
    val followeeId: Long,
) {
    fun toEntity() = Follow(followerId = followerId, followeeId = followeeId)
}

data class FollowCreateResultDto(
    val followId: Long,
) {
    companion object {
        operator fun invoke(follow: Follow) = FollowCreateResultDto(followId = follow.id!!)
    }
}

package com.hyoseok.service.dto

import com.hyoseok.follow.entity.Follow

data class FollowCreateResultDto(
    val followId: Long,
) {
    companion object {
        operator fun invoke(follow: Follow) = FollowCreateResultDto(followId = follow.id!!)
    }
}

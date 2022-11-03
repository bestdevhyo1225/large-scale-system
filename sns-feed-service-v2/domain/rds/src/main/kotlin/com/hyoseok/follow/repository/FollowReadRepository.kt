package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowReadRepository {
    fun findById(id: Long): Follow
    fun findAllByFollowerIdAndLimitAndOffset(followerId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
}

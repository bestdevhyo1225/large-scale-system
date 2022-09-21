package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowReadRepository {
    fun findAllByFollowerIdAndLimitAndOffset(followerId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
    fun findAllByFolloweeIdAndLimitAndOffset(followeeId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
}

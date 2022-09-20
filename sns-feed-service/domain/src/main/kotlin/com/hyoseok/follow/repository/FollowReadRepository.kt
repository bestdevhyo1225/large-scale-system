package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowReadRepository {
    fun findAllByFollowerIdAndLimitAndCount(followerId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
    fun findAllByFolloweeIdAndLimitAndCount(followeeId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
}

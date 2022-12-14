package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowReadRepository {
    fun countByFolloweeId(followeeId: Long): Long
    fun countByFollowerId(followerId: Long): Long
    fun findById(id: Long): Follow
    fun findAllByFolloweeIdAndLimitAndOffset(followeeId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
    fun findAllByFollowerIdAndLimitAndOffset(followerId: Long, limit: Long, offset: Long): Pair<Long, List<Follow>>
    fun findAllByFollowerIdAndLimitOrderByIdDesc(
        followerId: Long,
        influencerCheckTotalCount: Long,
        limit: Long,
    ): List<Follow>
}

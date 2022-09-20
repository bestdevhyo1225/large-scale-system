package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowReadRepository {
    fun findByFollowerId(followerId: Long): Follow
    fun findByFolloweeId(followeeId: Long): Follow
}

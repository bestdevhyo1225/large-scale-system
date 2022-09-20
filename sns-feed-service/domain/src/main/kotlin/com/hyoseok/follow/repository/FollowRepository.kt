package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow

interface FollowRepository {
    fun save(follow: Follow)
}

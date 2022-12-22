package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.FollowCount

interface FollowCountReadRepository {
    fun findByMemberId(memberId: Long): FollowCount?
}

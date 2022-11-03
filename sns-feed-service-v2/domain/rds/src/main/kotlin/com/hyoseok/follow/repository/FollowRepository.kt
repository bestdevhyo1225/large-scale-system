package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FollowRepository : JpaRepository<Follow, Long> {

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = :followerId")
    fun countByFollowerId(followerId: Long): Long
}

package com.hyoseok.repository.follow

import com.hyoseok.entity.follow.FollowJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FollowJpaRepository : JpaRepository<FollowJpaEntity, Long> {

    @Query("SELECT COUNT(f) FROM FollowJpaEntity f WHERE f.followeeId = :followeeId")
    fun countByFolloweeId(followeeId: Long): Long
}

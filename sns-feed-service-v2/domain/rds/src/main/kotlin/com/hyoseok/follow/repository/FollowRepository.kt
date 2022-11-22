package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FollowRepository : JpaRepository<Follow, Long>

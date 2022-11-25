package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow, Long>

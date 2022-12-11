package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.FollowCount
import org.springframework.data.jpa.repository.JpaRepository

interface FollowCountRepository : JpaRepository<FollowCount, Long>

package com.hyoseok.repository.follow

import com.hyoseok.entity.follow.FollowJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FollowJpaRepository : JpaRepository<FollowJpaEntity, Long>

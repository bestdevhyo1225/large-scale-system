package com.hyoseok.repository.post

import com.hyoseok.entity.post.PostJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<PostJpaEntity, Long>

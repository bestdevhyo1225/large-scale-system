package com.hyoseok.repository.post

import com.hyoseok.entity.post.PostJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostJpaRepository : JpaRepository<PostJpaEntity, Long> {

    @Query("SELECT COUNT(p) FROM PostJpaEntity p WHERE p.memberId = :memberId AND p.deletedAt IS NULL")
    fun countByMemberIdAndDeletedIsNull(memberId: Long): Long
}

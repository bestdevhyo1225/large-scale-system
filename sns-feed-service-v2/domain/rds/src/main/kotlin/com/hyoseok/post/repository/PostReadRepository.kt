package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post
import java.time.LocalDateTime

interface PostReadRepository {
    fun findById(id: Long): Post
    fun findByIdWithPostImage(id: Long): Post
    fun findAllByInId(ids: List<Long>): List<Post>
    fun findAllByMemberIdAndLimitAndOffset(memberId: Long, limit: Long, offset: Long): List<Post>
    fun findAllByMemberIdsAndCreatedAtAndLimitAndOffset(
        memberIds: List<Long>,
        fromCreatedAt: LocalDateTime,
        toCreatedAt: LocalDateTime,
        limit: Long,
        offset: Long,
    ): List<Post>
}

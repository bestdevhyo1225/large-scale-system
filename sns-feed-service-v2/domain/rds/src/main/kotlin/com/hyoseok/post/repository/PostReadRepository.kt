package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post

interface PostReadRepository {
    fun findById(id: Long): Post
    fun findByIdWithPostImage(id: Long): Post
    fun findAllByInId(ids: List<Long>): List<Post>
    fun findAllByMemberIdAndLimitAndCount(memberId: Long, limit: Long, offset: Long): List<Post>
    fun findAllByMemberIdsAndLimitAndCount(memberIds: List<Long>, limit: Long, offset: Long): List<Post>
}

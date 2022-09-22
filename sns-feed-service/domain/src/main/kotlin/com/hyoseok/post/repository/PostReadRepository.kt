package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post

interface PostReadRepository {
    fun findById(id: Long): Post
    fun findByIdWithImages(id: Long): Post
    fun findAllByPLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<Post>>

    fun findRecentlyRegisteredAllByIds(ids: List<Long>): List<Post>
}

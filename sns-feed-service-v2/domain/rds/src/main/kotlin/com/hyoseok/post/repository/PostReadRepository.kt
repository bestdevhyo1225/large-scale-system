package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post

interface PostReadRepository {
    fun findById(id: Long): Post
    fun findByIdWithPostImage(id: Long): Post
}

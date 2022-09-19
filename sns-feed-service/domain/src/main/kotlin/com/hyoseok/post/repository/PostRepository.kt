package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post

interface PostRepository {
    fun save(post: Post)
    fun update(post: Post)
}

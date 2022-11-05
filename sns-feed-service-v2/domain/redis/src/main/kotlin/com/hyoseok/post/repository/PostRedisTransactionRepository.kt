package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache

interface PostRedisTransactionRepository {
    fun createPostCache(postCache: PostCache): Boolean
}

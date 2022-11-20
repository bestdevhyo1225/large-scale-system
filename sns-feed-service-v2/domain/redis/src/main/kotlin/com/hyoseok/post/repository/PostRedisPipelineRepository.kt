package com.hyoseok.post.repository

import com.hyoseok.post.dto.PostCacheDto

interface PostRedisPipelineRepository {
    fun getPostCaches(ids: List<Long>): List<PostCacheDto>
}

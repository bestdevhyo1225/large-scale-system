package com.hyoseok.post.repository

import com.hyoseok.post.dto.PostCacheDto

interface PostRedisPipelineRepository {
    fun hgetPostCaches(ids: List<Long>): List<PostCacheDto>
    fun getPostCaches(ids: List<Long>): List<PostCacheDto>
}

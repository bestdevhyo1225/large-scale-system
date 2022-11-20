package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostRedisRepository
import com.hyoseok.post.repository.PostRedisTransactionRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisRepository: PostRedisRepository,
    private val postRedisTransactionRepository: PostRedisTransactionRepository,
) {

    fun create(dto: PostCacheDto) {
        postRedisTransactionRepository.createPostCache(postCache = dto.toEntity(), postViewCount = dto.viewCount)
    }

    fun increasePostView(id: Long) {
        postRedisRepository.hIncrement(key = PostCache.getPostViewBucketKey(id = id), hashKey = id, value = 1L)
    }
}

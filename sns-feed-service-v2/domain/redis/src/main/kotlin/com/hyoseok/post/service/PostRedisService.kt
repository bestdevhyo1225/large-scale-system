package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.repository.PostRedisTransactionRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisTransactionRepository: PostRedisTransactionRepository,
) {

    fun createOrUpdate(dto: PostCacheDto) {
        postRedisTransactionRepository.createPostCache(postCache = dto.toEntity())
    }
}

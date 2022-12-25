package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.getPostBucketKey
import com.hyoseok.post.entity.PostCache.Companion.getPostIdsByMemberIdKey
import com.hyoseok.post.repository.PostRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisRepository: PostRedisRepository,
) {

    fun createOrUpdate(dto: PostCacheDto) {
        val postCache: PostCache = dto.toEntity()
        postRedisRepository.hset(key = getPostBucketKey(id = postCache.id), hashKey = postCache.id, value = postCache)
        postRedisRepository.zadd(
            key = getPostIdsByMemberIdKey(memberId = postCache.memberId),
            value = postCache.id,
            score = postCache.id.toDouble(),
        )
    }
}

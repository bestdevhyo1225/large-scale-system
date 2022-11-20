package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostRedisPipelineRepository
import com.hyoseok.post.repository.PostRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisReadService(
    private val postRedisRepository: PostRedisRepository,
    private val postRedisPipelineRepository: PostRedisPipelineRepository,
) {

    fun findPostCache(id: Long): PostCacheDto? {
        val postCache: PostCache = postRedisRepository.hget(
            key = PostCache.getPostBucketKey(id = id),
            hashKey = id,
            clazz = PostCache::class.java,
        ) ?: return null

        val postViewCache: Long = postRedisRepository.hget(
            key = PostCache.getPostViewBucketKey(id = id),
            hashKey = id,
            clazz = Long::class.java,
        ) ?: 0L

        return PostCacheDto(postCache = postCache, viewCount = postViewCache)
    }

    fun findPostCaches(ids: List<Long>): List<PostCacheDto> =
        if (ids.isNotEmpty()) {
            postRedisPipelineRepository.getPostCaches(ids = ids)
        } else {
            listOf()
        }
}

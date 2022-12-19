package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.getPostIdKey
import com.hyoseok.post.entity.PostCache.Companion.getPostIdsByMemberIdKey
import com.hyoseok.post.repository.PostRedisPipelineRepository
import com.hyoseok.post.repository.PostRedisRepository
import com.hyoseok.util.PageRequestByPosition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisReadService(
    private val postRedisRepository: PostRedisRepository,
    private val postRedisPipelineRepository: PostRedisPipelineRepository,
) {

    fun findPostCache(id: Long): PostCacheDto? {
        val postCache: PostCache =
            postRedisRepository.get(key = getPostIdKey(id = id), clazz = PostCache::class.java) ?: return null

        return PostCacheDto(postCache = postCache)
    }

    fun findPostCaches(ids: List<Long>): List<PostCacheDto> =
        if (ids.isNotEmpty()) {
            postRedisPipelineRepository.getPostCaches(ids = ids)
        } else {
            listOf()
        }

    fun findPostCaches(memberId: Long, pageRequestByPosition: PageRequestByPosition): List<PostCacheDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (start <= -1L || size == 0L) {
            return listOf()
        }

        val key: String = getPostIdsByMemberIdKey(memberId = memberId)
        val end: Long = start.plus(size).minus(other = 1)
        val postIds: List<Long> = postRedisRepository.zrevRange(
            key = key,
            start = start,
            end = end,
            clazz = Long::class.java,
        )

        if (postIds.isEmpty()) {
            return listOf()
        }

        return postRedisPipelineRepository.getPostCaches(ids = postIds)
    }
}

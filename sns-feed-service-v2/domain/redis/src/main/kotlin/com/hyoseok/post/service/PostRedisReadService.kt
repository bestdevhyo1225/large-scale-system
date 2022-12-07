package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
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
        val postCache: PostCache = postRedisRepository.get(
            key = PostCache.getPostIdKey(id = id),
            clazz = PostCache::class.java,
        ) ?: return null

        val postViewCache: Long = postRedisRepository.get(
            key = PostCache.getPostIdViewsKey(id = id),
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

    fun findPostCaches(memberId: Long, pageRequestByPosition: PageRequestByPosition): List<PostCacheDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (start <= -1L || size == 0L) {
            return listOf()
        }

        val value: StringBuilder? = postRedisRepository.hget(
            key = PostCache.getPostMemberIdBucketKey(memberId = memberId),
            hashKey = memberId,
            clazz = StringBuilder::class.java,
        )

        if (value.isNullOrBlank()) {
            return listOf()
        }

        val end: Long = start.plus(size).minus(other = 1)
        val paginationIds: List<Long> = value.split(",")
            .map { it.toLong() }
            .slice(start.toInt()..end.toInt())

        return postRedisPipelineRepository.getPostCaches(ids = paginationIds)
    }
}

package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostRedisRepository
import org.springframework.stereotype.Service

@Service
class PostRedisReadService(
    private val postRedisRepository: PostRedisRepository,
) {

    fun findPostCache(id: Long): PostCacheDto? {
        val postCache: PostCache =
            postRedisRepository.get(key = PostCache.getPostIdKey(id = id), clazz = PostCache::class.java) ?: return null
        val postViewCache: Long =
            postRedisRepository.get(key = PostCache.getPostIdViewsKey(id = id), clazz = Long::class.java) ?: return null

        return PostCacheDto(postCache = postCache, viewCount = postViewCache)
    }
}

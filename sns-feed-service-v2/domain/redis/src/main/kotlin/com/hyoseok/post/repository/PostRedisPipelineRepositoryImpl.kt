package com.hyoseok.post.repository

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.getPostIdKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisPipelineRepositoryImpl(
    @Qualifier("postRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val postRedisRepository: PostRedisRepository,
) : PostRedisPipelineRepository {

    override fun getPostCaches(ids: List<Long>): List<PostCacheDto> {
        val result: MutableList<PostCacheDto> = mutableListOf()

        redisTemplate.executePipelined {
            ids.forEach { id ->
                val postCache: PostCache? =
                    postRedisRepository.get(key = getPostIdKey(id = id), clazz = PostCache::class.java)

                if (postCache != null) {
                    result.add(PostCacheDto(postCache = postCache))
                }
            }

            return@executePipelined null
        }

        return result
    }
}

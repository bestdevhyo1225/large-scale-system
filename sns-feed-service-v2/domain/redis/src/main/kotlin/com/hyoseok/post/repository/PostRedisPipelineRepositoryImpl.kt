package com.hyoseok.post.repository

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
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
                val postCache: PostCache? = postRedisRepository.get(
                    key = PostCache.getPostIdKey(id = id),
                    clazz = PostCache::class.java,
                )

                val postViewCount: Long = postRedisRepository.get(
                    key = PostCache.getPostIdViewsKey(id = id),
                    clazz = Long::class.java,
                ) ?: 0L

                if (postCache != null) {
                    result.add(PostCacheDto(postCache = postCache, viewCount = postViewCount))
                }
            }

            return@executePipelined null
        }

        return result
    }
}

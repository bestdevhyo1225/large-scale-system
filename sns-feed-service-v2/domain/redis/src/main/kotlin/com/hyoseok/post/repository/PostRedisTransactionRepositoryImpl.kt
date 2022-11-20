package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisTransactionRepositoryImpl(
    @Qualifier("postRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val postRedisRepository: PostRedisRepository,
) : PostRedisTransactionRepository {

    override fun createPostCache(postCache: PostCache, postViewCount: Long): Boolean =
        redisTemplate.execute { redisConnection ->
            try {
                redisConnection.multi()

                postRedisRepository.hset(
                    key = PostCache.getPostBucketKey(id = postCache.id),
                    hashKey = postCache.id,
                    value = postCache,
                )
                postRedisRepository.hset(
                    key = PostCache.getPostViewBucketKey(id = postCache.id),
                    hashKey = postCache.id,
                    value = postViewCount,
                )

                redisConnection.exec()
                return@execute true
            } catch (exception: RuntimeException) {
                redisConnection.discard()
                return@execute false
            }
        } as Boolean
}

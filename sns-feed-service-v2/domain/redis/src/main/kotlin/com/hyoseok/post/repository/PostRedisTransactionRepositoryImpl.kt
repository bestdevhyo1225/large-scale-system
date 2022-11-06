package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.POST_KEYS
import com.hyoseok.post.entity.PostCache.Companion.ZSET_POST_MAX_LIMIT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@Repository
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisTransactionRepositoryImpl(
    @Qualifier("postRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val postRedisRepository: PostRedisRepository,
) : PostRedisTransactionRepository {

    override fun createPostCache(postCache: PostCache): Boolean =
        redisTemplate.execute { redisConnection ->
            val (key: String, expireTime: Long) = PostCache.getPostKeyAndExpireTime(id = postCache.id)
            val score: Double = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

            try {
                redisConnection.multi()

                postRedisRepository.set(key = key, value = postCache, expireTime = expireTime, timeUnit = SECONDS)
                postRedisRepository.zadd(key = POST_KEYS, value = postCache.id, score = score)
                postRedisRepository.zremRangeByRank(
                    key = POST_KEYS,
                    start = ZSET_POST_MAX_LIMIT,
                    end = ZSET_POST_MAX_LIMIT,
                )

                redisConnection.exec()
                return@execute true
            } catch (exception: RuntimeException) {
                redisConnection.discard()
                return@execute false
            }
        } as Boolean
}

package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.POST_MEMBER_MAX_LIMIT
import com.hyoseok.post.entity.PostCache.Companion.getPostKeyAndExpireTime
import com.hyoseok.post.entity.PostCache.Companion.getPostMemberKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
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
            val (postKey: String, postExpireTime: Long) = getPostKeyAndExpireTime(id = postCache.id)
            val postMemberKey: String = getPostMemberKey(memberId = postCache.memberId)

            try {
                redisConnection.multi()

                postRedisRepository.set(
                    key = postKey,
                    value = postCache,
                    expireTime = postExpireTime,
                    timeUnit = SECONDS,
                )
                postRedisRepository.zadd(key = postMemberKey, value = postCache.id, score = postCache.id.toDouble())
                postRedisRepository.zremRangeByRank(
                    key = postMemberKey,
                    start = POST_MEMBER_MAX_LIMIT,
                    end = POST_MEMBER_MAX_LIMIT,
                )

                redisConnection.exec()
                return@execute true
            } catch (exception: RuntimeException) {
                redisConnection.discard()
                return@execute false
            }
        } as Boolean
}

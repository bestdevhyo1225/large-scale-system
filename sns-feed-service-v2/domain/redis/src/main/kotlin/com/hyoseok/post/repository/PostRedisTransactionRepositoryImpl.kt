package com.hyoseok.post.repository

import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.POST_MEMBER_MAX_LIMIT
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

    override fun createPostCache(postCache: PostCache, postViewCount: Long): Boolean =

        redisTemplate.execute { redisConnection ->
            try {
                redisConnection.multi()

                val (postKey: String, postExpireTime: Long) = PostCache.getPostKeyAndExpireTime(id = postCache.id)
                val (postViewKey: String, postViewExpireTime: Long) = PostCache.getPostViewsKeyAndExpireTime(
                    id = postCache.id,
                )
                val postMemberKey: String = PostCache.getPostMemberKey(memberId = postCache.memberId)

                postRedisRepository.set(
                    key = postKey,
                    value = postCache,
                    expireTime = postExpireTime,
                    timeUnit = SECONDS,
                )
                postRedisRepository.set(
                    key = postViewKey,
                    value = postViewCount,
                    expireTime = postViewExpireTime,
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

package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.WishCache
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit.SECONDS

@Repository
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisTransactionRepositoryImpl(
    @Qualifier("wishRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val wishRedisRepository: WishRedisRepository,
) : WishRedisTransactionRepository {

    override fun createWish(wishCache: WishCache): Boolean =
        redisTemplate.execute { redisConnection ->
            val key: String = wishCache.getKey()

            try {
                redisConnection.multi()

                wishRedisRepository.sadd(key = key, value = wishCache.memberId)

                if (wishRedisRepository.scard(key = key) == 1L) {
                    redisTemplate.expire(key, wishCache.expireTime, SECONDS)
                }

                redisConnection.exec()
                return@execute true
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard()
                return@execute false
            }
        } as Boolean
}

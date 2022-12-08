package com.hyoseok.wish.repository

import com.hyoseok.wish.entity.WishCache
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisPipelineRepositoryImpl(
    @Qualifier("wishRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val wishRedisRepository: WishRedisRepository,
) : WishRedisPipelineRepository {

    override fun getWishCount(postIds: List<Long>): Map<Long, Long> {
        val result: MutableMap<Long, Long> = mutableMapOf()

        redisTemplate.executePipelined {
            postIds.forEach {
                wishRedisRepository.scard(key = WishCache.getWishPostKey(postId = it))
                    ?.let { wishCount -> result[it] = wishCount }
            }

            return@executePipelined null
        }

        return result
    }
}

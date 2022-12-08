package com.hyoseok.wish.service

import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisPipelineRepository
import com.hyoseok.wish.repository.WishRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisReadService(
    private val wishRedisRepository: WishRedisRepository,
    private val wishRedisPipelineRepository: WishRedisPipelineRepository,
) {

    fun findWishCount(postId: Long): Long? = wishRedisRepository.scard(key = WishCache.getWishPostKey(postId = postId))

    fun findWishCounts(postIds: List<Long>): Map<Long, Long> =
        if (postIds.isNotEmpty()) {
            wishRedisPipelineRepository.getWishCount(postIds = postIds)
        } else {
            mapOf()
        }
}

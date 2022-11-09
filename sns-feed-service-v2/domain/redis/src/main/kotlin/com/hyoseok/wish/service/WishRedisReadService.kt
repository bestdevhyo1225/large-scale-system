package com.hyoseok.wish.service

import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class WishRedisReadService(
    private val wishRedisRepository: WishRedisRepository,
) {

    fun findWishCount(postId: Long): Long = wishRedisRepository.scard(key = WishCache.getKey(postId = postId))
}

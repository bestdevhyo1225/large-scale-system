package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.repository.WishRedisTransactionRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class WishRedisService(
    private val wishRedisTransactionRepository: WishRedisTransactionRepository,
) {

    fun create(dto: WishCacheDto): Boolean =
        wishRedisTransactionRepository.createWish(wishCache = dto.toEntity())
}

package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@Service
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisService(
    private val wishRedisRepository: WishRedisRepository,
) {

    fun create(dto: WishCacheDto) {
        val wishCache: WishCache = dto.toEntity()
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()
        wishRedisRepository.zaddAndExpire(
            key = wishCache.getWishPostKey(),
            value = wishCache.memberId,
            score = score,
            expireTime = wishCache.expireTime,
            timeUnit = SECONDS,
        )
    }
}

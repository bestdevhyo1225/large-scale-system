package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.repository.WishRedisTransactionRepository
import com.hyoseok.wish.service.WishRedisService.ErrorMessage.FAIL_CRAETE_WISH
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisService(
    private val wishRedisTransactionRepository: WishRedisTransactionRepository,
) {

    object ErrorMessage {
        const val FAIL_CRAETE_WISH = "좋아요 캐시를 저장하는데 실패했습니다"
    }

    fun create(dto: WishCacheDto) {
        if (!wishRedisTransactionRepository.createWish(wishCache = dto.toEntity())) {
            throw RuntimeException(FAIL_CRAETE_WISH)
        }
    }
}

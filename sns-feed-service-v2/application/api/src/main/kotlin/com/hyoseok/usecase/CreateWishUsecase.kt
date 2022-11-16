package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.CREATE_WISH_USECASE
import com.hyoseok.exception.ApiRateLimitException
import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.dto.WishEventDto
import com.hyoseok.wish.dto.WishEventLogDto
import com.hyoseok.wish.producer.WishKafkaProducer
import com.hyoseok.wish.service.WishEventLogService
import com.hyoseok.wish.service.WishRedisService
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CreateWishUsecase(
    private val wishEventLogService: WishEventLogService,
    private val wishRedisService: WishRedisService,
    private val wishKafkaProducer: WishKafkaProducer,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = CREATE_WISH_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(postId: Long, memberId: Long) {
        wishRedisService.create(dto = WishCacheDto(postId = postId, memberId = memberId))

        CoroutineScope(context = Dispatchers.IO).launch {
            sendWishEvent(postId = postId, memberId = memberId)
        }
    }

    private suspend fun sendWishEvent(postId: Long, memberId: Long) {
        val wishEventLogDto: WishEventLogDto = wishEventLogService.create(postId = postId, memberId = memberId)
        val wishEventDto = WishEventDto(wishEventLogId = wishEventLogDto.id, postId = postId, memberId = memberId)
        wishKafkaProducer.sendAsync(event = wishEventDto)
    }

    private fun fallbackExecute(exception: Exception) {
        logger.error { exception.localizedMessage }
        throw ApiRateLimitException(message = "일시적으로 좋아요를 할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

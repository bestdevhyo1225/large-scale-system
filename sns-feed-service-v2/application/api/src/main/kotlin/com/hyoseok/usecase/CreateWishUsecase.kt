package com.hyoseok.usecase

import com.hyoseok.wish.dto.WishCacheDto
import com.hyoseok.wish.dto.WishEventDto
import com.hyoseok.wish.producer.WishKafkaProducer
import com.hyoseok.wish.service.WishRedisService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class CreateWishUsecase(
    private val wishRedisService: WishRedisService,
    private val wishKafkaProducer: WishKafkaProducer,
) {

    fun execute(postId: Long, memberId: Long) {
        wishRedisService.create(dto = WishCacheDto(postId = postId, memberId = memberId))

        CoroutineScope(context = Dispatchers.IO).launch {
            sendWishEvent(postId = postId, memberId = memberId)
        }
    }

    private suspend fun sendWishEvent(postId: Long, memberId: Long) {
        wishKafkaProducer.sendAsync(WishEventDto(postId = postId, memberId = memberId))
    }
}

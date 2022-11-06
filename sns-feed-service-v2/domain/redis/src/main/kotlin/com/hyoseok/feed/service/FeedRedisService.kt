package com.hyoseok.feed.service

import com.hyoseok.feed.repository.FeedRedisTransactionRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisService(
    private val feedRedisTransactionRepository: FeedRedisTransactionRepository,
) {

    fun create(memberId: Long, postId: Long) {
        feedRedisTransactionRepository.createFeed(memberId = memberId, postId = postId)
    }
}

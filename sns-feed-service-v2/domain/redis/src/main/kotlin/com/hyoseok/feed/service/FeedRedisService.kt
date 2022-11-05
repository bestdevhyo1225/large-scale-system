package com.hyoseok.feed.service

import com.hyoseok.feed.repository.FeedRedisTransactionRepository
import org.springframework.stereotype.Service

@Service
class FeedRedisService(
    private val feedRedisTransactionRepository: FeedRedisTransactionRepository,
) {

    fun create(memberId: Long, postId: Long) {
        feedRedisTransactionRepository.createFeed(memberId = memberId, postId = postId)
    }
}

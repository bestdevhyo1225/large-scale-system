package com.hyoseok.feed.repository

interface FeedRedisTransactionRepository {
    fun createFeed(memberId: Long, postId: Long)
}

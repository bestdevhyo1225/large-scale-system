package com.hyoseok.service

import com.hyoseok.config.RedisCommons.ZSET_FEED_MAX_LIMIT
import com.hyoseok.config.RedisKeys
import com.hyoseok.feed.entity.FeedCache
import com.hyoseok.feed.repository.FeedCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class FeedService(
    private val feedCacheRepository: FeedCacheRepository,
) {

    fun create(postId: Long, memberId: Long, createdAt: LocalDateTime) {
        val key: String = RedisKeys.getMemberFeedKey(id = memberId)

        CoroutineScope(context = Dispatchers.IO).launch {
            zaddFeedCache(key = key, postId = postId, createdAt = createdAt)
            zremFeedCacheRangeByRank(key = key)
        }
    }

    private suspend fun zaddFeedCache(key: String, postId: Long, createdAt: LocalDateTime) {
        feedCacheRepository.zadd(
            key = key,
            value = FeedCache(postId = postId),
            score = Timestamp.valueOf(createdAt).time.toDouble(),
        )
    }

    private suspend fun zremFeedCacheRangeByRank(key: String) {
        feedCacheRepository.zremRangeByRank(key = key, start = ZSET_FEED_MAX_LIMIT, end = ZSET_FEED_MAX_LIMIT)
    }
}

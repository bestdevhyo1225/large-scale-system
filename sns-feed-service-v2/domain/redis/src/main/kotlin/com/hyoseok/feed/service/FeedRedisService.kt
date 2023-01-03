package com.hyoseok.feed.service

import com.hyoseok.feed.entity.FeedCache.Companion.ZSET_FEED_MAX_LIMIT
import com.hyoseok.feed.entity.FeedCache.Companion.getMemberIdFeedsKey
import com.hyoseok.feed.repository.FeedRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisService(
    private val feedRedisRepository: FeedRedisRepository,
) {

    fun create(memberId: Long, postId: Long) {
        val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()
        val key: String = getMemberIdFeedsKey(id = memberId)

        feedRedisRepository.zadd(key = key, value = postId, score = score)
        feedRedisRepository.zremRangeByRank(key = key, start = ZSET_FEED_MAX_LIMIT, end = ZSET_FEED_MAX_LIMIT)
    }
}

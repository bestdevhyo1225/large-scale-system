package com.hyoseok.feed.repository

import com.hyoseok.feed.entity.Feed
import com.hyoseok.feed.entity.Feed.Companion.ZSET_FEED_MAX_LIMIT
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@Repository
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisTransactionRepositoryImpl(
    @Qualifier("feedRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
    private val feedRedisRepository: FeedRedisRepository,
) : FeedRedisTransactionRepository {

    override fun createFeed(memberId: Long, postId: Long) {
        redisTemplate.execute { redisConnection ->
            val score: Double = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

            try {
                redisConnection.multi()

                val (key: String, expireTime: Long) = Feed.getMemberIdFeedsKeyAndExpireTime(id = memberId)

                feedRedisRepository.zadd(key = key, value = postId, score = score)
                feedRedisRepository.zremRangeByRank(key = key, start = ZSET_FEED_MAX_LIMIT, end = ZSET_FEED_MAX_LIMIT)
                redisTemplate.expire(key, expireTime, SECONDS)

                redisConnection.exec()
                return@execute
            } catch (exception: RuntimeException) {
                redisConnection.discard()
                return@execute
            }
        }
    }
}

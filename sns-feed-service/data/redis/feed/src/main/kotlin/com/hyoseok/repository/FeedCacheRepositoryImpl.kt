package com.hyoseok.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.config.RedisExpireTimes
import com.hyoseok.feed.repository.FeedCacheRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class FeedCacheRepositoryImpl(
    @Qualifier("redisFeedTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : FeedCacheRepository, AbstractCacheRepository() {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        val count: Long = redisTemplate.opsForZSet().zCard(key) ?: 0L

        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)

        if (count == 0L) {
            logger.info { "set feed expire time" }
            redisTemplate.expire(key, Duration.ofSeconds(RedisExpireTimes.FEED))
        }
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }
}

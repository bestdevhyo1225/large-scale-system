package com.hyoseok.repository.feed

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.feed.repository.FeedCacheRepository
import com.hyoseok.repository.AbstractCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["feed"], havingValue = "true")
class FeedCacheRepositoryImpl(
    @Qualifier("redisFeedTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : FeedCacheRepository, AbstractCacheRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }

    override fun zremRangeByScore(key: String, min: Double, max: Double) {
        redisTemplate.opsForZSet().removeRangeByScore(key, min, max)
    }

    override fun zremRangeByScoreUsedPipeline(keysAndScores: List<Triple<String, Double, Double>>) {
        redisTemplate.executePipelined {
            keysAndScores.forEach { (key: String, min: Double, max: Double) ->
                zremRangeByScore(key = key, min = min, max = max)
            }
            return@executePipelined null
        }
    }
}

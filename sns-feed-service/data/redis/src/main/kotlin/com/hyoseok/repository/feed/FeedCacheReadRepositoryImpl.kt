package com.hyoseok.repository.feed

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.feed.repository.FeedCacheReadRepository
import com.hyoseok.repository.AbstractCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["feed"], havingValue = "true")
class FeedCacheReadRepositoryImpl(
    @Qualifier("redisFeedTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : FeedCacheReadRepository, AbstractCacheRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun getExpire(key: String, timeUnit: TimeUnit): Long = redisTemplate.getExpire(key, timeUnit)

    override fun <T> zrevrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun <T> zrevrangebyscore(
        key: String,
        min: Double,
        max: Double,
        start: Long,
        end: Long,
        clazz: Class<T>,
    ): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }
}

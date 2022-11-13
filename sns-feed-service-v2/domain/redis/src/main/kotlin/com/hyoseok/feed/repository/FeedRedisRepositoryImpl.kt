package com.hyoseok.feed.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.common.AbstractRedisRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisRepositoryImpl(
    @Qualifier("feedRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : FeedRedisRepository, AbstractRedisRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }

    override fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }
}

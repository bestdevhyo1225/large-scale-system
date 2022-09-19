package com.hyoseok.repository.post

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.post.repository.PostCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class PostCacheRepositoryImpl(
    @Qualifier("redisPostTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : PostCacheRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun increment(key: String): Long = redisTemplate.opsForValue().increment(key) ?: 0

    override fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue()
            .set(key, jacksonObjectMapper.writeValueAsString(value), expireTime, timeUnit)
    }

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }
}

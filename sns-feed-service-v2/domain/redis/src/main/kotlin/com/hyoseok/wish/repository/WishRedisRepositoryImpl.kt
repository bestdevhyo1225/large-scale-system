package com.hyoseok.wish.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.wish.repository.WishRedisRepositoryImpl.ErrorMessage.SADD_RETURN_NULL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisRepositoryImpl(
    @Qualifier("wishRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : WishRedisRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    object ErrorMessage {
        const val SADD_RETURN_NULL = "sadd 명령 수행 후, NULL 값이 반환됨"
    }

    override fun <T : Any> sadd(key: String, value: T): Long =
        redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(value))
            ?: throw RuntimeException(SADD_RETURN_NULL)

    override fun scard(key: String): Long? = redisTemplate.opsForSet().size(key)

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun <T : Any> zaddAndExpire(key: String, value: T, score: Double, expireTime: Long, timeUnit: TimeUnit) {
        zadd(key = key, value = value, score = score)
        setExpire(key, expireTime, timeUnit)
    }

    override fun zcard(key: String): Long? = redisTemplate.opsForZSet().zCard(key)

    override fun <T : Any> zrevRangeByScore(
        key: String,
        minScore: Double,
        maxScore: Double,
        start: Long,
        end: Long,
        clazz: Class<T>,
    ): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }

    private fun setExpire(key: String, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.expire(key, expireTime, timeUnit)
    }
}

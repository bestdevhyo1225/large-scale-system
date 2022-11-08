package com.hyoseok.coupon.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.repository.CouponIssuedRedisRepositoryImpl.ErrorMessage.SADD_RETURN_NULL
import com.hyoseok.coupon.repository.CouponIssuedRedisRepositoryImpl.ErrorMessage.SCARD_RETURN_NULL
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class CouponIssuedRedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String?>,
) : CouponIssuedRedisRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    object ErrorMessage {
        const val SADD_RETURN_NULL = "sadd 명령 수행 후, NULL 값이 반환됨"
        const val SCARD_RETURN_NULL = "scard 명령 수행 후, NULL 값이 반환됨"
    }

    override fun expire(key: String, timeout: Long, timeUnit: TimeUnit): Boolean =
        redisTemplate.expire(key, timeout, timeUnit)

    override fun <T : Any> sadd(key: String, value: T): Long =
        redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(value))
            ?: throw RuntimeException(SADD_RETURN_NULL)

    override fun scard(key: String): Long =
        redisTemplate.opsForSet().size(key) ?: throw RuntimeException(SCARD_RETURN_NULL)
}

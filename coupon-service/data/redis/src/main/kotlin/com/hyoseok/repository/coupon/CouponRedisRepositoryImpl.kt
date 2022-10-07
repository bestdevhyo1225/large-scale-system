package com.hyoseok.repository.coupon

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.exception.DataRedisMessage.SADD_RETURN_NULL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["coupon"], havingValue = "true")
class CouponRedisRepositoryImpl(
    @Qualifier("redisCouponTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : CouponRedisRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> sadd(key: String, value: T): Long =
        redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(value))
            ?: throw RuntimeException(SADD_RETURN_NULL)

    override fun scard(key: String): Long = redisTemplate.opsForSet().size(key) ?: 0L

    override fun <T : Any> executeUsingTransaction(func: () -> T): T? {
        return redisTemplate.execute { redisConnection ->
            redisConnection.multi()
            val result: T = func()
            redisConnection.exec()
            return@execute result
        }
    }
}

package com.hyoseok.coupon.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.config.RedisKey
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus
import com.hyoseok.exception.DataRedisMessage.SADD_RETURN_NULL
import com.hyoseok.exception.DataRedisMessage.SCARD_RETURN_NULL
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS

@Repository
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["coupon"], havingValue = "true")
class CouponRedisRepositoryImpl(
    @Qualifier("redisCouponTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : CouponRedisRepository {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun createCouponIssued(coupon: Coupon, memberId: Long): Long =
        redisTemplate.execute { redisConnection ->
            val key: String = RedisKey.getCouponIssuedKey(couponId = coupon.id, issuedDate = LocalDate.now())
            var result: Long = CouponIssuedStatus.EXIT.code

            try {
                redisConnection.multi()

                val realtimeIssuedQuantity: Long = scard(key = key)

                if (realtimeIssuedQuantity < coupon.totalIssuedQuantity) {
                    result = sadd(key = key, value = memberId)
                }

                if (realtimeIssuedQuantity == 1L) {
                    logger.info { "expired: ${expire(key = key, timeout = 1, timeUnit = DAYS)}" }
                }

                redisConnection.exec()
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard()
                result = CouponIssuedStatus.FAILED.code
            }

            result
        } as Long

    private fun <T : Any> sadd(key: String, value: T): Long =
        redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(value))
            ?: throw RuntimeException(SADD_RETURN_NULL)

    private fun scard(key: String): Long =
        redisTemplate.opsForSet().size(key) ?: throw RuntimeException(SCARD_RETURN_NULL)

    private fun expire(key: String, timeout: Long, timeUnit: TimeUnit): Boolean =
        redisTemplate.expire(key, timeout, timeUnit)
}

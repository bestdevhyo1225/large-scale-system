package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.CouponIssuedStatus.FAILED
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit.DAYS

@Repository
class CouponIssuedRedisTransactionRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String?>,
    private val couponIssuedRedisRepository: CouponIssuedRedisRepository,
) : CouponIssuedRedisTransactionRepository {

    override fun createCouponIssued(couponIssued: CouponIssued, memberId: Long): Long =
        redisTemplate.execute { redisConnection ->
            val key: String = couponIssued.getKey()
            var result: Long = EXIT.code

            try {
                redisConnection.multi()

                val realtimeIssuedQuantity: Long = couponIssuedRedisRepository.scard(key = key)

                if (realtimeIssuedQuantity < couponIssued.totalIssuedQuantity) {
                    result = couponIssuedRedisRepository.sadd(key = key, value = memberId)
                }

                if (realtimeIssuedQuantity == 1L) {
                    couponIssuedRedisRepository.expire(key = key, timeout = 3, timeUnit = DAYS)
                }

                redisConnection.exec()
            } catch (exception: RuntimeException) { // RedisConnectionFailureException, QueryTimeoutException 포함
                redisConnection.discard()
                result = FAILED.code
            }

            result
        } as Long
}

package com.hyoseok.service.coupon

import com.hyoseok.config.RedisKey
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.exception.CommandApiMessage.COUPON_ISSUED_RESULT_IS_NULL
import com.hyoseok.service.dto.CouponIssuedCreateDto
import com.hyoseok.service.dto.CouponIssuedCreateResultDto
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CouponIssuedService(
    private val couponRedisRepository: CouponRedisRepository,
) {

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val totalIssuedQuantity: Long = 5_000 // CouponReadRepository 로 issuedLimitCount 조회
        val key: String =
            RedisKey.getCouponIssuedKey(couponId = dto.couponId, issuedDate = LocalDate.now()) // issuedStartedAt 변경
        val result: Long = couponRedisRepository.executeUsingTransaction {
            val realtimeIssuedQuantity: Long = couponRedisRepository.scard(key = key)

            if (realtimeIssuedQuantity < totalIssuedQuantity) {
                return@executeUsingTransaction couponRedisRepository.sadd(key = key, value = dto.memberId)
            }

            CouponIssued.EXIT
        } ?: throw RuntimeException(COUPON_ISSUED_RESULT_IS_NULL)

        if (result == CouponIssued.EXIT || result == CouponIssued.ALREADY_COMPLETE) {
            return CouponIssuedCreateResultDto(result = result)
        }

        // Kafka 메시지 발행
        // KafkaProducer.sendAsync()

        return CouponIssuedCreateResultDto(result = result)
    }
}

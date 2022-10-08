package com.hyoseok.service.coupon

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedFailRepository
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.exception.KafkaProducerSendFailedException
import com.hyoseok.service.MessageBrokerProducer
import com.hyoseok.service.dto.CouponIssuedCreateDto
import com.hyoseok.service.dto.CouponIssuedCreateResultDto
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CouponIssuedService(
    private val couponReadRepository: CouponReadRepository,
    private val couponRedisRepository: CouponRedisRepository,
    private val couponIssuedFailRepository: CouponIssuedFailRepository,
    private val messageBrokerProducer: MessageBrokerProducer,
) {

    private val logger = KotlinLogging.logger {}

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val coupon: Coupon = couponReadRepository.findById(couponId = dto.couponId)
        val result: Long = couponRedisRepository.createCouponIssued(coupon = coupon, memberId = dto.memberId)

        if (result == CouponIssued.FAILED || result == CouponIssued.COMPLETE || result == CouponIssued.EXIT) {
            return CouponIssuedCreateResultDto(result = result)
        }

        try {
            messageBrokerProducer.sendAsync(event = dto)
        } catch (exception: KafkaProducerSendFailedException) {
            couponIssuedFailRepository.save(couponIssuedFail = dto.toCouponIssuedFailEntity())
            logger.error { exception }
        }

        return CouponIssuedCreateResultDto(result = result)
    }
}

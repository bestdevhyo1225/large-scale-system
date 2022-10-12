package com.hyoseok.coupon.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.exception.CouponProducerSendFailedException
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.service.dto.CouponIssuedCreateResultDto
import com.hyoseok.message.entity.SendMessageFailLog
import com.hyoseok.message.repository.SendMessageFailLogRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.InetAddress
import java.util.UUID

@Service
class CouponIssuedService(
    @Value("\${infrastructure.kafka.producer.instance-id}")
    private val producerInstanceId: String,
    private val couponReadRepository: CouponReadRepository,
    private val couponRedisRepository: CouponRedisRepository,
    private val couponMessageBrokerProducer: CouponMessageBrokerProducer,
    private val sendMessageFailLogRepository: SendMessageFailLogRepository,
) {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val coupon: Coupon = couponReadRepository.findById(couponId = dto.couponId)
        val result: Long = couponRedisRepository.createCouponIssued(coupon = coupon, memberId = dto.memberId)

        if (result == FAILED.code || result == COMPLETE.code || result == EXIT.code) {
            return CouponIssuedCreateResultDto(result = result)
        }

        try {
            couponMessageBrokerProducer.sendAsync(event = dto)
        } catch (exception: CouponProducerSendFailedException) {
            sendMessageFailLogRepository.save(
                sendMessageFailLog = SendMessageFailLog(
                    instanceId = "$producerInstanceId-${InetAddress.getLocalHost().hostAddress}-${UUID.randomUUID()}",
                    data = jacksonObjectMapper.writeValueAsString(dto),
                    errorMessage = exception.cause?.localizedMessage ?: exception.localizedMessage,
                ),
            )
            logger.error { exception }
        }

        return CouponIssuedCreateResultDto(result = result)
    }
}

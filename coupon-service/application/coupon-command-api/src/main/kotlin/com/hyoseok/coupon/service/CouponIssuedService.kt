package com.hyoseok.coupon.service

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.service.dto.CouponIssuedCreateResultDto
import com.hyoseok.message.entity.SendMessageLog
import com.hyoseok.message.repository.SendMessageLogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class CouponIssuedService(
    private val couponReadRepository: CouponReadRepository,
    private val couponRedisRepository: CouponRedisRepository,
    private val couponMessageBrokerProducer: CouponMessageBrokerProducer,
    private val sendMessageLogRepository: SendMessageLogRepository,
) {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val coupon: Coupon = couponReadRepository.findById(couponId = dto.couponId)
        val result: Long = couponRedisRepository.createCouponIssued(coupon = coupon, memberId = dto.memberId)

        if (result == FAILED.code || result == COMPLETE.code || result == EXIT.code) {
            return CouponIssuedCreateResultDto(result = result)
        }

        CoroutineScope(context = Dispatchers.IO).launch {
            saveSendMessageLog(dto = dto)
            sendMessage(dto = dto)
        }

        return CouponIssuedCreateResultDto(result = result)
    }

    private suspend fun saveSendMessageLog(dto: CouponIssuedCreateDto) {
        sendMessageLogRepository.save(
            sendMessageLog = SendMessageLog(
                instanceId = couponMessageBrokerProducer.getInstanceId(),
                data = jacksonObjectMapper.writeValueAsString(dto),
            ),
        )
    }

    private suspend fun sendMessage(dto: CouponIssuedCreateDto) {
        couponMessageBrokerProducer.sendAsync(event = dto)
    }
}

package com.hyoseok.coupon.service

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponIssuedLog
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.repository.CouponIssuedLogRepository
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.service.dto.CouponIssuedCreateResultDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class CouponIssuedService(
    private val couponReadRepository: CouponReadRepository,
    private val couponRedisRepository: CouponRedisRepository,
    private val couponIssuedLogRepository: CouponIssuedLogRepository,
    private val couponMessageBrokerProducer: CouponMessageBrokerProducer,
) {

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val coupon: Coupon = couponReadRepository.findById(couponId = dto.couponId)
        val result: Long = couponRedisRepository.createCouponIssued(coupon = coupon, memberId = dto.memberId)

        if (result == FAILED.code || result == COMPLETE.code || result == EXIT.code) {
            return CouponIssuedCreateResultDto(result = result)
        }

        CoroutineScope(context = Dispatchers.IO).launch {
            saveCouponIssuedEventLog(dto = dto)
            sendCouponIssuedEvent(dto = dto)
        }

        return CouponIssuedCreateResultDto(result = result)
    }

    private suspend fun saveCouponIssuedEventLog(dto: CouponIssuedCreateDto) {
        couponIssuedLogRepository.save(
            couponIssuedLog = CouponIssuedLog(
                couponId = dto.couponId,
                memberId = dto.memberId,
                instanceId = couponMessageBrokerProducer.getInstanceId(),
            ),
        )
    }

    private suspend fun sendCouponIssuedEvent(dto: CouponIssuedCreateDto) {
        couponMessageBrokerProducer.sendAsync(event = dto)
    }
}

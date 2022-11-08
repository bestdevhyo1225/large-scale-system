package com.hyoseok.usecase

import com.hyoseok.coupon.dto.CouponDto
import com.hyoseok.coupon.dto.CouponIssuedCacheDto
import com.hyoseok.coupon.dto.CouponIssuedCacheResultDto
import com.hyoseok.coupon.entity.CouponIssuedCache.Status.READY
import com.hyoseok.coupon.service.CouponIssuedRedisService
import com.hyoseok.coupon.service.CouponReadService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class CreateCouponIssuedUsecase(
    private val couponReadService: CouponReadService,
    private val couponIssuedRedisService: CouponIssuedRedisService,
) {

    private val logger = KotlinLogging.logger {}

    fun execute(couponId: Long, memberId: Long): CouponIssuedCacheResultDto {
        val couponDto: CouponDto = couponReadService.find(couponId = couponId)
        val couponIssuedCacheDto = CouponIssuedCacheDto(
            couponId = couponDto.id,
            totalIssuedQuantity = couponDto.totalIssuedQuantity,
            memberId = memberId,
        )
        val result: Long = couponIssuedRedisService.create(dto = couponIssuedCacheDto)

        if (result == READY.code) {
            sendEvent(dto = couponIssuedCacheDto)
        }

        return CouponIssuedCacheResultDto(result = result)
    }

    private fun sendEvent(dto: CouponIssuedCacheDto) {
        CoroutineScope(context = Dispatchers.IO).launch {
            saveCouponIssuedEventLog(dto = dto)
            sendCouponIssuedEvent(dto = dto)
        }
    }

    private suspend fun saveCouponIssuedEventLog(dto: CouponIssuedCacheDto) {
        logger.info { "dto: $dto" }
    }

    private suspend fun sendCouponIssuedEvent(dto: CouponIssuedCacheDto) {
        logger.info { "dto: $dto" }
    }
}

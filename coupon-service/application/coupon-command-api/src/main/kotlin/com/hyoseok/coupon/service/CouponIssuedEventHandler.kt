package com.hyoseok.coupon.service

import com.hyoseok.coupon.publisher.dto.CouponIssuedSendCompletedDto
import com.hyoseok.coupon.repository.CouponIssuedLogRepository
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CouponIssuedEventHandler(
    private val couponIssuedLogRepository: CouponIssuedLogRepository,
) {

    private val logger = KotlinLogging.logger {}

    @EventListener
    fun handle(dto: CouponIssuedSendCompletedDto) {
        logger.info { "CouponIssuedEvent send completed" }

        with(receiver = dto) {
            couponIssuedLogRepository.updateIsSendCompleted(
                couponId = couponId,
                memberId = memberId,
                isSendCompleted = true,
            )
        }
    }
}

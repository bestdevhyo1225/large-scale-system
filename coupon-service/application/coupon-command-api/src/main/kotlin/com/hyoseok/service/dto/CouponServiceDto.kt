package com.hyoseok.service.dto

import com.hyoseok.coupon.entity.Coupon
import java.time.LocalDateTime

data class CouponCreateDto(
    val name: String,
    val totalIssuedQuantity: Int,
    val issuedStartedAt: LocalDateTime,
    val issuedEndedAt: LocalDateTime,
    val availableStartedAt: LocalDateTime,
    val availableEndedAt: LocalDateTime,
) {
    fun toEntity() =
        Coupon(
            name = name,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedStartedAt = issuedStartedAt,
            issuedEndedAt = issuedEndedAt,
            availableStartedAt = availableStartedAt,
            availableEndedAt = availableEndedAt,
        )
}

data class CouponCreateResultDto(
    val couponId: Long,
) {
    companion object {
        operator fun invoke(coupon: Coupon) =
            with(receiver = coupon) {
                CouponCreateResultDto(couponId = id)
            }
    }
}

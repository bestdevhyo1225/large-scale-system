package com.hyoseok.coupon.dto

import com.hyoseok.coupon.entity.Coupon
import java.time.LocalDateTime

data class CouponDto(
    val id: Long,
    val name: String,
    val totalIssuedQuantity: Int,
    val issuedStartedAt: LocalDateTime,
    val issuedEndedAt: LocalDateTime,
    val availableStartedAt: LocalDateTime,
    val availableEndedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(coupon: Coupon): CouponDto =
            with(receiver = coupon) {
                CouponDto(
                    id = id!!,
                    name = name,
                    totalIssuedQuantity = totalIssuedQuantity,
                    issuedStartedAt = issuedStartedAt,
                    issuedEndedAt = issuedEndedAt,
                    availableStartedAt = availableStartedAt,
                    availableEndedAt = availableEndedAt,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}

data class CouponCreateDto(
    val name: String,
    val totalIssuedQuantity: Int,
    val issuedStartedAt: LocalDateTime,
    val issuedEndedAt: LocalDateTime,
    val availableStartedAt: LocalDateTime,
    val availableEndedAt: LocalDateTime,
) {
    fun toEntity(): Coupon =
        Coupon(
            name = name,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedStartedAt = issuedStartedAt,
            issuedEndedAt = issuedEndedAt,
            availableStartedAt = availableStartedAt,
            availableEndedAt = availableEndedAt,
        )
}

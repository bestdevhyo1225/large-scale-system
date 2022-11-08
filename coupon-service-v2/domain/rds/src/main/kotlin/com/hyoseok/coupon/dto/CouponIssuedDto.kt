package com.hyoseok.coupon.dto

import com.hyoseok.coupon.entity.CouponIssued
import java.time.LocalDateTime

data class CouponIssuedDto(
    val id: Long,
    val couponId: Long,
    val memberId: Long,
    val createdAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(couponIssued: CouponIssued): CouponIssuedDto =
            with(receiver = couponIssued) {
                CouponIssuedDto(id = id!!, couponId = couponId, memberId = memberId, createdAt = createdAt)
            }
    }
}

data class CouponIssuedCreateDto(
    val couponId: Long,
    val memberId: Long,
) {
    fun toEntity() = CouponIssued(couponId = couponId, memberId = memberId)
}

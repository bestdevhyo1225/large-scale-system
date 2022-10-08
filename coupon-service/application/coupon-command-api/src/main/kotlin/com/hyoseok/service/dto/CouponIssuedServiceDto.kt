package com.hyoseok.service.dto

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedFail

data class CouponIssuedCreateDto(
    val memberId: Long,
    val couponId: Long,
) {
    fun toCouponIssuedFailEntity() = CouponIssuedFail(couponId = couponId, memberId = memberId)
}

data class CouponIssuedCreateResultDto(
    val code: Long,
    val message: String,
) {
    companion object {
        operator fun invoke(result: Long): CouponIssuedCreateResultDto {
            val message: String = when (result) {
                CouponIssued.FAILED -> "쿠폰 발급 실패"
                CouponIssued.EXIT -> "쿠폰 발급 종료"
                CouponIssued.COMPLETE -> "쿠폰 발급 완료"
                CouponIssued.READY -> "쿠폰 발급 준비"
                else -> "존재하지 않는 code"
            }
            return CouponIssuedCreateResultDto(code = result, message = message)
        }
    }
}

package com.hyoseok.coupon.service.dto

import com.hyoseok.coupon.entity.CouponIssuedFail
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus

data class CouponIssuedCreateDto(
    val memberId: Long,
    val couponId: Long,
) {
    fun toCouponIssuedFailEntity() = CouponIssuedFail(couponId = couponId, memberId = memberId)
}

data class CouponIssuedCreateResultDto(
    val code: String,
    val message: String,
) {
    companion object {
        operator fun invoke(result: Long): CouponIssuedCreateResultDto {
            val (code: String, message: String) = when (result) {
                CouponIssuedStatus.FAILED.code -> {
                    Pair(
                        first = CouponIssuedStatus.FAILED.name,
                        second = CouponIssuedStatus.FAILED.message,
                    )
                }

                CouponIssuedStatus.EXIT.code -> {
                    Pair(
                        first = CouponIssuedStatus.EXIT.name,
                        second = CouponIssuedStatus.EXIT.message,
                    )
                }

                CouponIssuedStatus.COMPLETE.code -> {
                    Pair(
                        first = CouponIssuedStatus.COMPLETE.name,
                        second = CouponIssuedStatus.COMPLETE.message,
                    )
                }

                CouponIssuedStatus.READY.code -> {
                    Pair(
                        first = CouponIssuedStatus.READY.name,
                        second = CouponIssuedStatus.READY.message,
                    )
                }

                else -> Pair(first = "NONE", second = "존재하지 않는 code")
            }
            return CouponIssuedCreateResultDto(code = code, message = message)
        }
    }
}

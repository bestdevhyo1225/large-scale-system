package com.hyoseok.coupon.dto

import com.hyoseok.coupon.entity.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.entity.CouponIssuedStatus.READY

data class CreateCouponIssuedDto(
    val couponId: Long,
    val totalIssuedQuantity: Int,
    val memberId: Long,
)

data class CreateCouponIssuedResultDto(
    val code: String,
    val message: String,
) {
    companion object {
        operator fun invoke(result: Long): CreateCouponIssuedResultDto {
            val (code: String, message: String) = when (result) {
                FAILED.code -> Pair(first = FAILED.name, second = FAILED.message)
                EXIT.code -> Pair(first = EXIT.name, second = EXIT.message)
                COMPLETE.code -> Pair(first = COMPLETE.name, second = COMPLETE.message)
                READY.code -> Pair(first = READY.name, second = READY.message)
                else -> Pair(first = "NONE", second = "존재하지 않는 code")
            }
            return CreateCouponIssuedResultDto(code = code, message = message)
        }
    }
}

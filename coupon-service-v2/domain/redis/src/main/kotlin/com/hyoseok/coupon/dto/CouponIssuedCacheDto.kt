package com.hyoseok.coupon.dto

import com.hyoseok.coupon.entity.CouponIssuedCache
import com.hyoseok.coupon.entity.CouponIssuedCache.Status.COMPLETE
import com.hyoseok.coupon.entity.CouponIssuedCache.Status.EXIT
import com.hyoseok.coupon.entity.CouponIssuedCache.Status.FAILED
import com.hyoseok.coupon.entity.CouponIssuedCache.Status.READY
import java.time.LocalDate

data class CouponIssuedCacheDto(
    val couponId: Long,
    val totalIssuedQuantity: Int,
    val memberId: Long,
) {
    fun toEntity() =
        CouponIssuedCache(
            couponId = couponId,
            totalIssuedQuantity = totalIssuedQuantity,
            issuedDate = LocalDate.now(),
        )
}

data class CouponIssuedCacheResultDto(
    val code: String,
    val message: String,
) {
    companion object {
        operator fun invoke(result: Long): CouponIssuedCacheResultDto {
            val (code: String, message: String) = when (result) {
                FAILED.code -> Pair(first = FAILED.name, second = FAILED.message)
                EXIT.code -> Pair(first = EXIT.name, second = EXIT.message)
                COMPLETE.code -> Pair(first = COMPLETE.name, second = COMPLETE.message)
                READY.code -> Pair(first = READY.name, second = READY.message)
                else -> Pair(first = "NONE", second = "존재하지 않는 code")
            }
            return CouponIssuedCacheResultDto(code = code, message = message)
        }
    }
}

package com.hyoseok.config

import java.time.LocalDate

object RedisKey {
    fun getCouponIssuedKey(couponId: Long, issuedDate: LocalDate) =
        "coupon:$couponId:issued-date:$issuedDate:issued:members"
}

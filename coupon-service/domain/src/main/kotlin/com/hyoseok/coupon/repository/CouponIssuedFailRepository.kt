package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFail

interface CouponIssuedFailRepository {
    fun save(couponIssuedFail: CouponIssuedFail)
}

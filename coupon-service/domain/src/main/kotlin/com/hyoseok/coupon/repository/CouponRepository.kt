package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon

interface CouponRepository {
    fun save(coupon: Coupon)
}

package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued

interface CouponIssuedRepository {
    fun save(couponIssued: CouponIssued)
}

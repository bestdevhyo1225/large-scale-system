package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedCache

interface CouponIssuedRedisTransactionRepository {
    fun createCouponIssued(couponIssuedCache: CouponIssuedCache, memberId: Long): Long
}

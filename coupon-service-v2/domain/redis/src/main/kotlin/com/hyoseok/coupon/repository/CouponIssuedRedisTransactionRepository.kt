package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued

interface CouponIssuedRedisTransactionRepository {
    fun createCouponIssued(couponIssued: CouponIssued, memberId: Long): Long
}

package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon

interface CouponRedisRepository {
    fun createCouponIssued(coupon: Coupon, memberId: Long): Long
}

package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon

interface CouponReadRepository {
    fun findById(id: Long): Coupon
}

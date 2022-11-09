package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued

interface CouponIssuedReadRepository {
    fun findById(id: Long): CouponIssued
}

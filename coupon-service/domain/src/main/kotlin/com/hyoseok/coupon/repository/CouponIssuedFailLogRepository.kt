package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLog

interface CouponIssuedFailLogRepository {
    fun save(couponIssuedFailLog: CouponIssuedFailLog)
}

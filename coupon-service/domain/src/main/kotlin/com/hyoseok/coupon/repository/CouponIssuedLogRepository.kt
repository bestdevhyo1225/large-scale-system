package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLog

interface CouponIssuedLogRepository {
    fun save(couponIssuedLog: CouponIssuedLog)
    fun updateIsSendCompleted(couponId: Long, memberId: Long, isSendCompleted: Boolean)
}

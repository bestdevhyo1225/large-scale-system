package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLog

interface CouponIssuedLogReadRepository {
    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<CouponIssuedLog>>
    fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<CouponIssuedLog>>
    fun findByCouponIdAndMemberId(couponId: Long, memberId: Long): CouponIssuedLog
}

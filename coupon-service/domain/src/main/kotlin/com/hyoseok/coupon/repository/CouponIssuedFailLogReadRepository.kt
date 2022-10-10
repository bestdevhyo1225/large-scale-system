package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType

interface CouponIssuedFailLogReadRepository {
    fun findByApplicationTypeAndlimitAndOffset(
        applicationType: CouponIssuedFailLogApplicationType,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<CouponIssuedFailLog>>
}

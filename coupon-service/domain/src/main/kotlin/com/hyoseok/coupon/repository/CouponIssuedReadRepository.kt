package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued

interface CouponIssuedReadRepository {
    fun findByCouponIdAndMemberId(couponId: Long, memberId: Long): CouponIssued
    fun findByMemberIdAndlimitAndOffset(memberId: Long, limit: Long, offset: Long): Pair<Long, List<CouponIssued>>
}

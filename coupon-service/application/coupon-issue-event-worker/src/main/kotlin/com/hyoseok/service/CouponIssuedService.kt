package com.hyoseok.service

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CouponIssuedService(
    private val couponIssuedRepository: CouponIssuedRepository,
) {

    fun create(couponId: Long, memberId: Long) {
        couponIssuedRepository.save(couponIssued = CouponIssued(couponId = couponId, memberId = memberId))
    }
}

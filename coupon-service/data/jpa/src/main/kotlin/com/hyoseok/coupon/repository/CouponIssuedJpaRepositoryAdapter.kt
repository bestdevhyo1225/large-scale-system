package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponIssuedJpaRepositoryAdapter(
    private val couponIssuedJpaRepository: CouponIssuedJpaRepository,
) : CouponIssuedRepository {

    override fun save(couponIssued: CouponIssued) {
        couponIssuedJpaRepository.save(CouponIssuedEntity(couponIssued = couponIssued))
            .also { couponIssued.changeId(id = it.id!!) }
    }
}

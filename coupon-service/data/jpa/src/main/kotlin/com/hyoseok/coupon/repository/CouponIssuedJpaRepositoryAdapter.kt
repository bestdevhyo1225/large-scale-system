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
        val couponIssuedEntity = CouponIssuedEntity(couponIssued = couponIssued)
        couponIssuedJpaRepository.save(couponIssuedEntity)
        couponIssued.changeId(id = couponIssuedEntity.id!!)
    }
}

package com.hyoseok.repository.coupon

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedRepository
import com.hyoseok.entity.coupon.CouponIssuedEntity
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

package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedEntity
import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponIssuedJpaRepositoryAdapter(
    private val couponIssuedJpaRepository: CouponIssuedJpaRepository,
) : CouponIssuedRepository {

    private val logger = KotlinLogging.logger {}

    override fun save(couponIssued: CouponIssued) {
        val couponIssuedEntity = CouponIssuedEntity(couponIssued = couponIssued)
        try {
            couponIssuedJpaRepository.save(couponIssuedEntity)
            couponIssued.changeId(id = couponIssuedEntity.id!!)
        } catch (exception: DataIntegrityViolationException) {
            logger.error { exception }
        }
    }
}

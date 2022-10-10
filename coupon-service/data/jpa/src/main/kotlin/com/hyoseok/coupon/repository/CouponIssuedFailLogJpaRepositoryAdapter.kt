package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.CouponIssuedFailLogEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponIssuedFailLogJpaRepositoryAdapter(
    private val couponIssuedFailLogJpaRepository: CouponIssuedFailLogJpaRepository,
) : CouponIssuedFailLogRepository {

    override fun save(couponIssuedFailLog: CouponIssuedFailLog) {
        val couponIssuedFailLogEntity = CouponIssuedFailLogEntity(couponIssuedFailLog = couponIssuedFailLog)
        couponIssuedFailLogJpaRepository.save(couponIssuedFailLogEntity)
        couponIssuedFailLog.changeId(id = couponIssuedFailLogEntity.id!!)
    }
}

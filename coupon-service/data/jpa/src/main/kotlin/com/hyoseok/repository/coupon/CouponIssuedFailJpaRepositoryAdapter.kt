package com.hyoseok.repository.coupon

import com.hyoseok.coupon.entity.CouponIssuedFail
import com.hyoseok.coupon.repository.CouponIssuedFailRepository
import com.hyoseok.entity.coupon.CouponIssuedFailEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponIssuedFailJpaRepositoryAdapter(
    private val couponIssuedFailJpaRepository: CouponIssuedFailJpaRepository,
) : CouponIssuedFailRepository {

    override fun save(couponIssuedFail: CouponIssuedFail) {
        val couponIssuedFailEntity = CouponIssuedFailEntity(couponIssuedFail = couponIssuedFail)
        couponIssuedFailJpaRepository.save(couponIssuedFailEntity)
        couponIssuedFail.changeId(id = couponIssuedFailEntity.id!!)
    }
}

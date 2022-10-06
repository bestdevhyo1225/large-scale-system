package com.hyoseok.repository.coupon

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.repository.CouponRepository
import com.hyoseok.entity.coupon.CouponEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponJpaRepositoryAdapter(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {

    override fun save(coupon: Coupon) {
        val couponEntity = CouponEntity(coupon = coupon)
        couponJpaRepository.save(couponEntity)
        coupon.changeId(id = couponEntity.id!!)
    }
}

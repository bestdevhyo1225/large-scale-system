package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponEntity
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager")
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class CouponJpaRepositoryAdapter(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {

    override fun save(coupon: Coupon) {
        couponJpaRepository.save(CouponEntity(coupon = coupon))
            .also { coupon.changeId(id = it.id!!) }
    }
}

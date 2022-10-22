package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponEntity
import com.hyoseok.coupon.entity.QCouponEntity.couponEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_COUPON_ENTITY
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class CouponJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
) : CouponReadRepository {

    override fun findById(couponId: Long): Coupon {
        val couponEntity: CouponEntity = jpaQueryFactory
            .selectFrom(couponEntity)
            .where(couponEntity.id.eq(couponId))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_COUPON_ENTITY)

        return couponEntity.toDomain()
    }
}

package com.hyoseok.coupon.repository

import com.hyoseok.coupon.constants.CouponErrorMessage.NOT_FOUND_COUPON
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.QCoupon.coupon
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class CouponReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : CouponReadRepository {

    override fun findById(id: Long): Coupon =
        jpaQueryFactory
            .selectFrom(coupon)
            .where(couponIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_COUPON)

    private fun couponIdEq(id: Long): BooleanExpression = coupon.id.eq(id)
}

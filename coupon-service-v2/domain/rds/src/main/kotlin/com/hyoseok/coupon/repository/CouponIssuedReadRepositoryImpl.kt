package com.hyoseok.coupon.repository

import com.hyoseok.coupon.constants.CouponErrorMessage.NOT_FOUND_COUPON_ISSUED
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.QCouponIssued.couponIssued
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class CouponIssuedReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : CouponIssuedReadRepository {

    override fun findById(id: Long): CouponIssued =
        jpaQueryFactory
            .selectFrom(couponIssued)
            .where(couponIssuedIdEqId(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_COUPON_ISSUED)

    private fun couponIssuedIdEqId(id: Long): BooleanExpression = couponIssued.id.eq(id)
}

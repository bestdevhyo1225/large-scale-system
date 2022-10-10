package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.QCouponIssuedFailLogEntity.couponIssuedFailLogEntity
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class CouponIssuedFailLogJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val couponIssuedFailLogJpaRepository: CouponIssuedFailLogJpaRepository,
) : CouponIssuedFailLogReadRepository {

    override fun findByApplicationTypeAndlimitAndOffset(
        applicationType: CouponIssuedFailLogApplicationType,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<CouponIssuedFailLog>> =
        Pair(
            first = couponIssuedFailLogJpaRepository.countByApplicationType(applicationType = applicationType.name),
            second = jpaQueryFactory
                .selectFrom(couponIssuedFailLogEntity)
                .where(couponIssuedFailLogEntityApplicationTypeEq(applicationType = applicationType.name))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    private fun couponIssuedFailLogEntityApplicationTypeEq(applicationType: String): BooleanExpression =
        couponIssuedFailLogEntity.applicationType.eq(applicationType)
}

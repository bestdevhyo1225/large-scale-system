package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLog
import com.hyoseok.coupon.entity.CouponIssuedLogEntity
import com.hyoseok.coupon.entity.QCouponIssuedLogEntity.couponIssuedLogEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_COUPON_ISSUED_LOG_ENTITY
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class CouponIssuedLogJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val couponIssuedLogJpaRepository: CouponIssuedLogJpaRepository,
) : CouponIssuedLogReadRepository {

    override fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<CouponIssuedLog>> =
        Pair(
            first = couponIssuedLogJpaRepository.countAll(),
            second = jpaQueryFactory
                .selectFrom(couponIssuedLogEntity)
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    override fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<CouponIssuedLog>> =
        Pair(
            first = couponIssuedLogJpaRepository.countAllByInstanceId(instanceId = instanceId),
            second = jpaQueryFactory
                .selectFrom(couponIssuedLogEntity)
                .where(couponIssuedLogEntityEqInstanceId(instanceId = instanceId))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    override fun findByCouponIdAndMemberId(couponId: Long, memberId: Long): CouponIssuedLog {
        val couponIssuedLogEntity: CouponIssuedLogEntity = jpaQueryFactory
            .selectFrom(couponIssuedLogEntity)
            .where(
                couponIssuedLogEntityEqCouponId(couponId = couponId),
                couponIssuedLogEntityEqMemberId(memberId = memberId),
            )
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_COUPON_ISSUED_LOG_ENTITY)

        return couponIssuedLogEntity.toDomain()
    }

    private fun couponIssuedLogEntityEqInstanceId(instanceId: String): BooleanExpression =
        couponIssuedLogEntity.instanceId.eq(instanceId)

    private fun couponIssuedLogEntityEqCouponId(couponId: Long): BooleanExpression =
        couponIssuedLogEntity.couponId.eq(couponId)

    private fun couponIssuedLogEntityEqMemberId(memberId: Long): BooleanExpression =
        couponIssuedLogEntity.memberId.eq(memberId)
}

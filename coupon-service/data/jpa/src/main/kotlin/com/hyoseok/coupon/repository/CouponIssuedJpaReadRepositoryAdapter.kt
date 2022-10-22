package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedEntity
import com.hyoseok.coupon.entity.QCouponIssuedEntity.couponIssuedEntity
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class CouponIssuedJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val couponIssuedJpaRepository: CouponIssuedJpaRepository,
) : CouponIssuedReadRepository {

    override fun findByCouponIdAndMemberId(couponId: Long, memberId: Long): CouponIssued {
        val couponIssuedEntity: CouponIssuedEntity = jpaQueryFactory
            .selectFrom(couponIssuedEntity)
            .where(
                couponIssuedEntityCouponIdEq(couponId = couponId),
                couponIssuedEntityMemberIdEq(memberId = memberId),
            )
            .fetchOne() ?: throw NoSuchElementException("")

        return couponIssuedEntity.toDomain()
    }

    override fun findByMemberIdAndlimitAndOffset(
        memberId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<CouponIssued>> =
        Pair(
            first = couponIssuedJpaRepository.countCouponIssuedEntityByMemberId(memberId = memberId),
            second = jpaQueryFactory
                .selectFrom(couponIssuedEntity)
                .where(couponIssuedEntityMemberIdEq(memberId = memberId))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    private fun couponIssuedEntityCouponIdEq(couponId: Long): BooleanExpression =
        couponIssuedEntity.couponId.eq(couponId)

    private fun couponIssuedEntityMemberIdEq(memberId: Long): BooleanExpression =
        couponIssuedEntity.memberId.eq(memberId)
}

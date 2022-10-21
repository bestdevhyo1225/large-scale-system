package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLog
import com.hyoseok.coupon.entity.CouponIssuedLogEntity
import com.hyoseok.coupon.entity.QCouponIssuedLogEntity.couponIssuedLogEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_COUPON_ISSUED_LOG_ENTITY
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CouponIssuedLogJpaRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val couponIssuedLogJpaRepository: CouponIssuedLogJpaRepository,
) : CouponIssuedLogRepository {

    override fun save(couponIssuedLog: CouponIssuedLog) {
        couponIssuedLogJpaRepository.save(CouponIssuedLogEntity(couponIssuedLog = couponIssuedLog))
            .also { couponIssuedLog.changeId(id = it.id!!) }
    }

    override fun updateIsSendCompleted(couponId: Long, memberId: Long, isSendCompleted: Boolean) {
        val couponIssuedLogEntity: CouponIssuedLogEntity = jpaQueryFactory
            .selectFrom(couponIssuedLogEntity)
            .where(
                couponIssuedLogEntityEqCouponId(couponId = couponId),
                couponIssuedLogEntityEqMemberId(memberId = memberId),
            )
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_COUPON_ISSUED_LOG_ENTITY)

        couponIssuedLogEntity.changeSendCompleted(isSendCompleted = isSendCompleted)
    }

    private fun couponIssuedLogEntityEqCouponId(couponId: Long): BooleanExpression =
        couponIssuedLogEntity.couponId.eq(couponId)

    private fun couponIssuedLogEntityEqMemberId(memberId: Long): BooleanExpression =
        couponIssuedLogEntity.memberId.eq(memberId)
}

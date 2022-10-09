package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CouponIssuedJpaRepository : JpaRepository<CouponIssuedEntity, Long> {

    @Query("SELECT COUNT(ci) FROM CouponIssuedEntity ci WHERE ci.memberId = :memberId")
    fun countCouponIssuedEntityByMemberId(memberId: Long): Long
}

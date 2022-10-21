package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CouponIssuedLogJpaRepository : JpaRepository<CouponIssuedLogEntity, Long> {

    @Query("SELECT COUNT(cil) FROM CouponIssuedLogEntity cil")
    fun countAll(): Long

    @Query("SELECT COUNT(cil) FROM CouponIssuedLogEntity cil WHERE cil.instanceId = :instanceId")
    fun countAllByInstanceId(instanceId: String): Long
}

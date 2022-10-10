package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CouponIssuedFailLogJpaRepository : JpaRepository<CouponIssuedFailLogEntity, Long> {

    @Query("SELECT COUNT(cifl) FROM CouponIssuedFailLogEntity cifl WHERE cifl.applicationType = :applicationType")
    fun countByApplicationType(applicationType: String): Long
}

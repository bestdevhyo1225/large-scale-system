package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailLogEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedFailLogJpaRepository : JpaRepository<CouponIssuedFailLogEntity, Long>

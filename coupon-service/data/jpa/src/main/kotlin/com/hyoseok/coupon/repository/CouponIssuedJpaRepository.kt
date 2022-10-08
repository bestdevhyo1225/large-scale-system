package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedJpaRepository : JpaRepository<CouponIssuedEntity, Long>

package com.hyoseok.repository.coupon

import com.hyoseok.entity.coupon.CouponIssuedEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedJpaRepository : JpaRepository<CouponIssuedEntity, Long>

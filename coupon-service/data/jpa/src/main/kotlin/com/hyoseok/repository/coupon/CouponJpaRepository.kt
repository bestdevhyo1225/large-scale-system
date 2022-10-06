package com.hyoseok.repository.coupon

import com.hyoseok.entity.coupon.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<CouponEntity, Long>

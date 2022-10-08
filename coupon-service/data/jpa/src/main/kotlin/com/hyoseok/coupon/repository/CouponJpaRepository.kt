package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponJpaRepository : JpaRepository<CouponEntity, Long>

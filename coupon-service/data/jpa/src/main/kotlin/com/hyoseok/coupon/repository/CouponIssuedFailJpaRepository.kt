package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedFailEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedFailJpaRepository : JpaRepository<CouponIssuedFailEntity, Long>

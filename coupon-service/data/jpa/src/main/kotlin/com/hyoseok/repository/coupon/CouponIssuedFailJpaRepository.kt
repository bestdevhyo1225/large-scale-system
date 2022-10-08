package com.hyoseok.repository.coupon

import com.hyoseok.entity.coupon.CouponIssuedFailEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedFailJpaRepository : JpaRepository<CouponIssuedFailEntity, Long>

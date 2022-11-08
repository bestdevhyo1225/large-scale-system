package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long>

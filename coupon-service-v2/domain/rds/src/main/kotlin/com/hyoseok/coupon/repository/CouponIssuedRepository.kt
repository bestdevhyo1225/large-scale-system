package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssued
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssuedRepository : JpaRepository<CouponIssued, Long>

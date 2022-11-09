package com.hyoseok.coupon.service

import com.hyoseok.coupon.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.dto.CouponIssuedDto
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CouponIssuedService(
    private val couponIssuedRepository: CouponIssuedRepository,
) {

    fun create(dto: CouponIssuedCreateDto): CouponIssuedDto {
        val savedCouponIssued: CouponIssued = dto.toEntity().also { couponIssuedRepository.save(it) }
        return CouponIssuedDto(couponIssued = savedCouponIssued)
    }
}

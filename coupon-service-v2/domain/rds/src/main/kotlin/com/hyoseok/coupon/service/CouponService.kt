package com.hyoseok.coupon.service

import com.hyoseok.coupon.dto.CouponCreateDto
import com.hyoseok.coupon.dto.CouponDto
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.repository.CouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CouponService(
    private val couponRepository: CouponRepository,
) {

    fun create(dto: CouponCreateDto): CouponDto {
        val savedCoupon: Coupon = dto.toEntity().also { couponRepository.save(it) }
        return CouponDto(coupon = savedCoupon)
    }
}

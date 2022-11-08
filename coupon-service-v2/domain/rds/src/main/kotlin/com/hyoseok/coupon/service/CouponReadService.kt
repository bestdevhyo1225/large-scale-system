package com.hyoseok.coupon.service

import com.hyoseok.coupon.dto.CouponDto
import com.hyoseok.coupon.repository.CouponReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CouponReadService(
    private val couponReadRepository: CouponReadRepository,
) {

    fun find(couponId: Long): CouponDto = CouponDto(coupon = couponReadRepository.findById(id = couponId))
}

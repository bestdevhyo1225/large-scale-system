package com.hyoseok.coupon.service

import com.hyoseok.coupon.repository.CouponRepository
import com.hyoseok.coupon.service.dto.CouponCreateDto
import com.hyoseok.coupon.service.dto.CouponCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CouponService(
    private val couponRepository: CouponRepository,
) {

    @Transactional
    fun create(dto: CouponCreateDto) =
        CouponCreateResultDto(coupon = dto.toEntity().also { couponRepository.save(coupon = it) })
}

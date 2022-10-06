package com.hyoseok.service.coupon

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.repository.CouponRepository
import com.hyoseok.service.dto.CouponCreateDto
import com.hyoseok.service.dto.CouponCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CouponService(
    private val couponRepository: CouponRepository,
) {

    @Transactional
    fun create(dto: CouponCreateDto): CouponCreateResultDto {
        val coupon: Coupon = dto.toEntity()
        couponRepository.save(coupon = coupon)
        return CouponCreateResultDto(coupon = coupon)
    }
}

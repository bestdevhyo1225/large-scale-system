package com.hyoseok.coupon.service

import com.hyoseok.coupon.dto.CouponIssuedCacheDto
import com.hyoseok.coupon.repository.CouponIssuedRedisTransactionRepository
import org.springframework.stereotype.Service

@Service
class CouponIssuedRedisService(
    private val couponIssuedRedisTransactionRepository: CouponIssuedRedisTransactionRepository,
) {

    fun create(dto: CouponIssuedCacheDto): Long =
        couponIssuedRedisTransactionRepository.createCouponIssued(
            couponIssuedCache = dto.toEntity(),
            memberId = dto.memberId,
        )
}

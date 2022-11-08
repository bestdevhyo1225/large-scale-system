package com.hyoseok.coupon.service

import com.hyoseok.coupon.dto.CreateCouponIssuedDto
import com.hyoseok.coupon.dto.CreateCouponIssuedResultDto
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.repository.CouponIssuedRedisTransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class CouponIssuedService(
    private val couponIssuedRedisTransactionRepository: CouponIssuedRedisTransactionRepository,
) {

    fun create(dto: CreateCouponIssuedDto): CreateCouponIssuedResultDto {
        val couponIssued: CouponIssued =
            with(receiver = dto) {
                CouponIssued(
                    couponId = couponId,
                    totalIssuedQuantity = totalIssuedQuantity,
                    issuedDate = LocalDate.now(),
                )
            }
        val result: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
            couponIssued = couponIssued,
            memberId = dto.memberId,
        )

        if (result == FAILED.code || result == COMPLETE.code || result == EXIT.code) {
            return CreateCouponIssuedResultDto(result = result)
        }

        return CreateCouponIssuedResultDto(result = result)
    }
}

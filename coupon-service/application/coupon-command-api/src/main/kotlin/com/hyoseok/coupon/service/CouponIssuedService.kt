package com.hyoseok.coupon.service

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.enum.CouponIssuedStatus.FAILED
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.coupon.service.dto.CouponIssuedCreateDto
import com.hyoseok.coupon.service.dto.CouponIssuedCreateResultDto
import org.springframework.stereotype.Service

@Service
class CouponIssuedService(
    private val couponReadRepository: CouponReadRepository,
    private val couponRedisRepository: CouponRedisRepository,
    private val couponMessageBrokerProducer: CouponMessageBrokerProducer,
) {

    fun create(dto: CouponIssuedCreateDto): CouponIssuedCreateResultDto {
        val coupon: Coupon = couponReadRepository.findById(couponId = dto.couponId)
        val result: Long = couponRedisRepository.createCouponIssued(coupon = coupon, memberId = dto.memberId)

        if (result == FAILED.code || result == COMPLETE.code || result == EXIT.code) {
            return CouponIssuedCreateResultDto(result = result)
        }

        // 만약에 메시지 브로커가 다운된 경우, 메시지를 보낼 수 없기 때문에 배치 스케줄러를 통해 Redis, MySQL 간의 싱크를 맞추자
        // 1. 쿠폰 리스트 조회
        // 2. couponId 기준의 Key로 Redis에서 쿠폰 발급 목록 조회 (memberId 리스트)
        // 3. memberId 값으로 쿠폰 발급 조회
        // 4. 쿠폰 발급 내역이 MySQL에 없으면, 회원에게 쿠폰 발급 (MySQL 저장)
        couponMessageBrokerProducer.sendAsync(event = dto)

        return CouponIssuedCreateResultDto(result = result)
    }
}

package com.hyoseok.service

import com.hyoseok.coupon.repository.CouponIssuedRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class CouponIssuedServiceTests : DescribeSpec(
    {
        val mockCouponIssuedRepository: CouponIssuedRepository = mockk()
        val couponIssuedService = CouponIssuedService(couponIssuedRepository = mockCouponIssuedRepository)

        describe("create 메서드는") {
            it("해당 회원에게 쿠폰을 발급한다") {
                // given
                val couponId: Long = 1
                val memberId: Long = 1

                every { mockCouponIssuedRepository.save(couponIssued = any()) } returns Unit

                // when
                withContext(Dispatchers.IO) {
                    couponIssuedService.create(couponId = couponId, memberId = memberId)
                }

                // then
                verify { mockCouponIssuedRepository.save(couponIssued = any()) }
            }
        }
    },
)

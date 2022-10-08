package com.hyoseok.coupon.service

import com.hyoseok.coupon.repository.CouponRepository
import com.hyoseok.coupon.service.dto.CouponCreateDto
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class CouponServiceTests : DescribeSpec(
    {
        val mockCouponRepository: CouponRepository = mockk()
        val couponService = CouponService(couponRepository = mockCouponRepository)

        describe("create 메서드는") {
            it("쿠폰을 생성한다") {
                // given
                val now: LocalDateTime = LocalDateTime.now().withNano(0)
                val dto = CouponCreateDto(
                    name = "쿠폰1",
                    totalIssuedQuantity = 5_000,
                    issuedStartedAt = now,
                    issuedEndedAt = now.plusDays(5),
                    availableStartedAt = now,
                    availableEndedAt = now.plusMonths(1),
                )

                every { mockCouponRepository.save(coupon = any()) } returns Unit

                // when
                withContext(Dispatchers.IO) {
                    couponService.create(dto = dto)
                }

                // then
                verify(exactly = 1) { mockCouponRepository.save(coupon = any()) }
            }
        }
    },
)

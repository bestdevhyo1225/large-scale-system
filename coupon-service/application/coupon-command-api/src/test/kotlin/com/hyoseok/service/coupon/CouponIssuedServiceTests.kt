package com.hyoseok.service.coupon

import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponIssuedFailRepository
import com.hyoseok.coupon.repository.CouponReadRepository
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_COUPON_ENTITY
import com.hyoseok.service.MessageBrokerProducer
import com.hyoseok.service.dto.CouponIssuedCreateDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime

internal class CouponIssuedServiceTests : DescribeSpec(
    {
        val mockCouponReadRepository: CouponReadRepository = mockk()
        val mockCouponRedisRepository: CouponRedisRepository = mockk()
        val mockCouponIssuedFailRepository: CouponIssuedFailRepository = mockk()
        val mockMessageBrokerProducer: MessageBrokerProducer = mockk()
        val couponIssuedService = CouponIssuedService(
            couponReadRepository = mockCouponReadRepository,
            couponRedisRepository = mockCouponRedisRepository,
            couponIssuedFailRepository = mockCouponIssuedFailRepository,
            messageBrokerProducer = mockMessageBrokerProducer,
        )

        describe("create 메서드는") {
            val dto = CouponIssuedCreateDto(memberId = 1, couponId = 1)
            val now: LocalDateTime = LocalDateTime.now()
            val coupon = Coupon(
                id = dto.couponId,
                name = "쿠폰!",
                totalIssuedQuantity = 5_000,
                issuedStartedAt = now,
                issuedEndedAt = now.plusDays(5),
                availableStartedAt = now,
                availableEndedAt = now.plusMonths(1),
                createdAt = now,
                updatedAt = now,
                deletedAt = null,
            )

            it("회원에게 쿠폰을 발급한다") {
                // given
                every { mockCouponReadRepository.findById(couponId = dto.couponId) } returns coupon
                every {
                    mockCouponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = dto.memberId,
                    )
                } returns CouponIssued.READY
                every { mockMessageBrokerProducer.sendAsync(event = dto) } returns Unit

                // when
                couponIssuedService.create(dto = dto).code.shouldBe(CouponIssued.READY)

                // then
                verify { mockCouponReadRepository.findById(couponId = dto.couponId) }
                verify {
                    mockCouponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = dto.memberId,
                    )
                }
                verify { mockMessageBrokerProducer.sendAsync(event = dto) }
            }

            context("해당 회원이 이미 발급받은 경우") {
                it("발급 완료 결과를 반환한다") {
                    // given
                    every { mockCouponReadRepository.findById(couponId = dto.couponId) } returns coupon
                    every {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    } returns CouponIssued.COMPLETE

                    // when
                    couponIssuedService.create(dto = dto).code.shouldBe(CouponIssued.COMPLETE)

                    // then
                    verify { mockCouponReadRepository.findById(couponId = dto.couponId) }
                    verify {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    }
                }
            }

            context("쿠폰 발급이 종료된 경우") {
                it("종료 결과를 반환한다") {
                    // given
                    every { mockCouponReadRepository.findById(couponId = dto.couponId) } returns coupon
                    every {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    } returns CouponIssued.EXIT

                    // when
                    couponIssuedService.create(dto = dto).code.shouldBe(CouponIssued.EXIT)

                    // then
                    verify { mockCouponReadRepository.findById(couponId = dto.couponId) }
                    verify {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    }
                }
            }

            context("쿠폰 발급 도중 장애가 발생하면") {
                it("실패 결과를 반환한다") {
                    // given
                    every { mockCouponReadRepository.findById(couponId = dto.couponId) } returns coupon
                    every {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    } returns CouponIssued.FAILED

                    // when
                    couponIssuedService.create(dto = dto).code.shouldBe(CouponIssued.FAILED)

                    // then
                    verify { mockCouponReadRepository.findById(couponId = dto.couponId) }
                    verify {
                        mockCouponRedisRepository.createCouponIssued(
                            coupon = coupon,
                            memberId = dto.memberId,
                        )
                    }
                }
            }

            context("쿠폰이 존재하지 않는 경우") {
                it("NoSuchElementException 예외를 던진다") {
                    // given
                    every {
                        mockCouponReadRepository.findById(couponId = dto.couponId)
                    } throws NoSuchElementException(NOT_FOUND_COUPON_ENTITY)

                    // when
                    shouldThrow<NoSuchElementException> { couponIssuedService.create(dto = dto) }

                    // then
                    verify { mockCouponReadRepository.findById(couponId = dto.couponId) }
                }
            }
        }
    },
)

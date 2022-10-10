package com.hyoseok.coupon.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssued
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class CouponIssuedJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedRepository: CouponIssuedRepository

    @Autowired
    private lateinit var couponIssuedReadRepository: CouponIssuedReadRepository

    init {
        this.describe("save 메서드는") {
            it("CouponIssued 엔티티를 저장한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val couponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                // when
                couponIssuedRepository.save(couponIssued = couponIssued)

                // then
                couponIssued.id.shouldNotBeNull()
                couponIssued.id.shouldNotBeZero()
            }

            context("중복된 couponId, memberId 인 경우") {
                it("에러 로그만 출력하고 정상 응답한다") {
                    // given
                    val couponId = 1L
                    val memberId = 1L
                    val couponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                    couponIssuedRepository.save(couponIssued = couponIssued)

                    val duplicateCouponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                    // when
                    couponIssuedRepository.save(couponIssued = duplicateCouponIssued)

                    // then
                    duplicateCouponIssued.id.shouldBeZero()
                }
            }
        }

        this.describe("findByCouponIdAndMemberId 메서드는") {
            it("회원이 발급 받은 쿠폰 발급 데이터를 반환한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val couponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                couponIssuedRepository.save(couponIssued = couponIssued)

                // when
                val findCouponIssued: CouponIssued =
                    couponIssuedReadRepository.findByCouponIdAndMemberId(couponId = couponId, memberId = memberId)

                // then
                findCouponIssued.shouldBe(couponIssued)
            }
        }

        this.describe("findByMemberIdAndlimitAndOffset 메서드는") {
            it("회원이 발급받은 쿠폰 발급 리스트를 반환한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val couponIssued = CouponIssued(couponId = couponId, memberId = memberId)

                couponIssuedRepository.save(couponIssued = couponIssued)

                // when
                val result: Pair<Long, List<CouponIssued>> =
                    couponIssuedReadRepository.findByMemberIdAndlimitAndOffset(
                        memberId = memberId,
                        limit = 10,
                        offset = 0,
                    )

                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }
    }
}

package com.hyoseok.coupon.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssued
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class CouponIssuedJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedRepository: CouponIssuedRepository

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
        }
    }
}

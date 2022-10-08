package com.hyoseok.repository.coupon

import com.hyoseok.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssuedFail
import com.hyoseok.coupon.repository.CouponIssuedFailRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class CouponIssuedFailJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedFailRepository: CouponIssuedFailRepository

    init {
        this.describe("save 메서드는") {
            it("CouponIssuedFail 엔티티를 저장한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val couponIssuedFail = CouponIssuedFail(couponId = couponId, memberId = memberId)

                // when
                couponIssuedFailRepository.save(couponIssuedFail = couponIssuedFail)

                // then
                couponIssuedFail.id.shouldNotBeNull()
                couponIssuedFail.id.shouldNotBeZero()
            }
        }
    }
}

package com.hyoseok.repository.coupon

import com.hyoseok.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.repository.CouponRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

internal class CouponJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponRepository: CouponRepository

    init {
        this.describe("save 메서드는") {
            it("Coupon 엔티티를 저장한다") {
                // given
                val name = "XXX 쿠폰 데이"
                val issuedLimitCount = 5_000
                val now: LocalDateTime = LocalDateTime.now()
                val issuedStartedAt: LocalDateTime = now
                val issuedEndedAt: LocalDateTime = now.plusDays(5)
                val availableStartedAt: LocalDateTime = now
                val availableEndedAt: LocalDateTime = now.plusMonths(1)
                val coupon = Coupon(
                    name = name,
                    issuedLimitCount = issuedLimitCount,
                    issuedStartedAt = issuedStartedAt,
                    issuedEndedAt = issuedEndedAt,
                    availableStartedAt = availableStartedAt,
                    availableEndedAt = availableEndedAt,
                )

                // when
                couponRepository.save(coupon = coupon)

                // then
                coupon.id.shouldNotBeNull()
            }
        }
    }
}

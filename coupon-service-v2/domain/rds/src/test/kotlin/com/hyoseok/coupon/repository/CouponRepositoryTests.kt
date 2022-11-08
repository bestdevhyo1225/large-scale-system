package com.hyoseok.coupon.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.coupon.entity.Coupon
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        CouponRepository::class,
        CouponReadRepository::class,
        CouponReadRepositoryImpl::class,
    ],
)
internal class CouponRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var couponReadRepository: CouponReadRepository

    init {
        this.describe("save 메서드는") {
            it("Coupon을 저장한다") {
                // given
                val name = "XXX 쿠폰 데이"
                val totalIssuedQuantity = 5_000
                val now: LocalDateTime = LocalDateTime.now()
                val issuedStartedAt: LocalDateTime = now
                val issuedEndedAt: LocalDateTime = now.plusDays(5)
                val availableStartedAt: LocalDateTime = now
                val availableEndedAt: LocalDateTime = now.plusMonths(1)
                val coupon = Coupon(
                    name = name,
                    totalIssuedQuantity = totalIssuedQuantity,
                    issuedStartedAt = issuedStartedAt,
                    issuedEndedAt = issuedEndedAt,
                    availableStartedAt = availableStartedAt,
                    availableEndedAt = availableEndedAt,
                )

                // when
                withContext(Dispatchers.IO) {
                    couponRepository.save(coupon)
                }

                // then
                couponReadRepository.findById(id = coupon.id!!)
                    .shouldBe(coupon)
            }
        }
    }
}

package com.hyoseok.coupon.repository

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.coupon.entity.CouponIssued
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

@DataJpaTest
@ContextConfiguration(
    classes = [
        BasicDataSourceConfig::class,
        JpaConfig::class,
        CouponIssuedRepository::class,
        CouponIssuedReadRepository::class,
        CouponIssuedReadRepositoryImpl::class,
    ],
)
internal class CouponIssuedRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedRepository: CouponIssuedRepository

    @Autowired
    private lateinit var couponIssuedReadRepository: CouponIssuedReadRepository

    init {
        this.describe("save 메서드는") {
            it("쿠폰 발급 정보를 저장한다") {
                // given
                val couponIssued = CouponIssued(
                    couponId = 1L,
                    memberId = 1L,
                )

                // when
                withContext(Dispatchers.IO) {
                    couponIssuedRepository.save(couponIssued)
                }

                // then
                couponIssuedReadRepository.findById(id = couponIssued.id!!)
                    .shouldBe(couponIssued)
            }
        }
    }
}

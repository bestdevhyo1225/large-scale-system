package com.hyoseok.coupon.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

internal class CouponIssuedFailLogJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedFailLogRepository: CouponIssuedFailLogRepository

    @Autowired
    private lateinit var couponIssuedFailLogReadRepository: CouponIssuedFailLogReadRepository

    init {
        this.describe("save 메서드는") {
            it("CouponIssuedFailLog 엔티티를 저장한다") {
                // given
                val applicationType = CouponIssuedFailLogApplicationType.PRODUCER
                val data = "data"
                val error = RuntimeException("Occured RuntimeException")
                val couponIssuedFailLog = CouponIssuedFailLog(
                    applicationType = applicationType,
                    data = data,
                    errorMessage = error.localizedMessage,
                )

                // when
                couponIssuedFailLogRepository.save(couponIssuedFailLog = couponIssuedFailLog)

                // then
                couponIssuedFailLog.id.shouldNotBeNull()
                couponIssuedFailLog.id.shouldNotBeZero()
            }
        }

        this.describe("findByApplicationTypeAndlimitAndOffset 메서드는") {
            it("로그 내역을 가져온다") {
                // given
                val applicationType = CouponIssuedFailLogApplicationType.PRODUCER
                val data = "data"
                val error = RuntimeException("Occured RuntimeException")
                val couponIssuedFailLog = CouponIssuedFailLog(
                    applicationType = applicationType,
                    data = data,
                    errorMessage = error.localizedMessage,
                )

                couponIssuedFailLogRepository.save(couponIssuedFailLog = couponIssuedFailLog)

                // when
                val result: Pair<Long, List<CouponIssuedFailLog>> =
                    couponIssuedFailLogReadRepository.findByApplicationTypeAndlimitAndOffset(
                        applicationType = applicationType,
                        limit = 10,
                        offset = 0,
                    )

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }
    }
}

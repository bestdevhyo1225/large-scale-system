package com.hyoseok.coupon.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

internal class CouponIssuedFailLogJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedFailLogRepository: CouponIssuedFailLogRepository

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
    }
}

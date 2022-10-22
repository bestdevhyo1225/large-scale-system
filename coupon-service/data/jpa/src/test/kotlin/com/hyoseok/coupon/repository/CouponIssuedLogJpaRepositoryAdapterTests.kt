package com.hyoseok.coupon.repository

import com.hyoseok.config.JpaRepositoryAdapterTests
import com.hyoseok.coupon.entity.CouponIssuedLog
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

internal class CouponIssuedLogJpaRepositoryAdapterTests : JpaRepositoryAdapterTests, DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponIssuedLogRepository: CouponIssuedLogRepository

    @Autowired
    private lateinit var couponIssuedLogReadRepository: CouponIssuedLogReadRepository

    init {
        this.describe("deleteAllByCreatedAtBefore 메서드는") {
            it("createdAt 이전의 날짜의 로그는 모두 삭제된다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val instanceId = "producer-instance-id"
                val couponIssuedLog =
                    CouponIssuedLog(couponId = couponId, memberId = memberId, instanceId = instanceId)

                couponIssuedLogRepository.save(couponIssuedLog = couponIssuedLog)

                // when
                couponIssuedLogRepository.deleteAllByCreatedAtBefore(createdAt = LocalDateTime.now().plusMinutes(5))

                // then
                shouldThrow<NoSuchElementException> {
                    couponIssuedLogReadRepository.findByCouponIdAndMemberId(couponId = couponId, memberId = memberId)
                }
            }
        }

        this.describe("save 메서드는") {
            it("CouponIssuedLog 엔티티를 저장한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val instanceId = "producer-instance-id"
                val couponIssuedLog =
                    CouponIssuedLog(couponId = couponId, memberId = memberId, instanceId = instanceId)

                // when
                couponIssuedLogRepository.save(couponIssuedLog = couponIssuedLog)

                // then
                couponIssuedLog.id.shouldNotBeNull()
                couponIssuedLog.id.shouldNotBeZero()
            }
        }

        this.describe("updateIsSendCompleted 메서드는") {
            it("전송 완료 상태를 변경한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val instanceId = "producer-instance-id"
                val couponIssuedLog =
                    CouponIssuedLog(couponId = couponId, memberId = memberId, instanceId = instanceId)

                couponIssuedLogRepository.save(couponIssuedLog = couponIssuedLog)

                // when
                couponIssuedLogRepository.updateIsSendCompleted(
                    couponId = couponId,
                    memberId = memberId,
                    isSendCompleted = true,
                )

                // then
                val findCouponIssuedLog: CouponIssuedLog =
                    couponIssuedLogReadRepository.findByCouponIdAndMemberId(couponId = couponId, memberId = memberId)

                findCouponIssuedLog.isSendCompleted.shouldBeTrue()
                findCouponIssuedLog.sendCompletedAt.shouldNotBeNull()
            }
        }

        this.describe("findAllByLimitAndOffset 메서드는") {
            it("전체 CouponIssuedLog 전체 수와 리스트를 반환한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val instanceId = "producer-instance-id"
                val couponIssuedLog =
                    CouponIssuedLog(couponId = couponId, memberId = memberId, instanceId = instanceId)

                couponIssuedLogRepository.save(couponIssuedLog = couponIssuedLog)

                // when
                val result: Pair<Long, List<CouponIssuedLog>> =
                    couponIssuedLogReadRepository.findAllByLimitAndOffset(limit = 10, offset = 0)

                // then
                result.first.shouldNotBeZero()
                result.first.shouldBe(1)
                result.second.shouldNotBeEmpty()
                result.second.shouldHaveSize(1)
            }
        }

        this.describe("findAllByInstanceIdAndLimitAndOffset 메서드는") {
            it("instanceId 기준으로 CouponIssuedLog 전체 수와 리스트를 반환한다") {
                // given
                val couponId = 1L
                val memberId = 1L
                val instanceId = "producer-instance-id"
                val couponIssuedLog =
                    CouponIssuedLog(couponId = couponId, memberId = memberId, instanceId = instanceId)

                couponIssuedLogRepository.save(couponIssuedLog = couponIssuedLog)

                // when
                val result: Pair<Long, List<CouponIssuedLog>> =
                    couponIssuedLogReadRepository.findAllByInstanceIdAndLimitAndOffset(
                        instanceId = instanceId,
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

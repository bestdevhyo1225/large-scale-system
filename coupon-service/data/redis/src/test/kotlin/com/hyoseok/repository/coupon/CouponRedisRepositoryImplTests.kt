package com.hyoseok.repository.coupon

import com.hyoseok.config.coupon.RedisCouponConfig
import com.hyoseok.config.coupon.RedisCouponEmbbededServerConfig
import com.hyoseok.config.coupon.RedisCouponTemplateConfig
import com.hyoseok.coupon.entity.Coupon
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponRedisRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisCouponEmbbededServerConfig::class,
        RedisCouponConfig::class,
        RedisCouponTemplateConfig::class,
        CouponRedisRepositoryImpl::class,
    ],
)
internal class CouponRedisRepositoryImplTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var couponRedisRepository: CouponRedisRepository

    @Autowired
    @Qualifier(value = "redisCouponTemplate")
    private lateinit var redisCouponTemplate: RedisTemplate<String, String?>

    init {
        afterSpec {
            redisCouponTemplate.delete(redisCouponTemplate.keys("*"))
        }

        this.describe("createCouponIssued 메서드는") {
            it("트랜잭션 기반으로 쿠폰 발급 데이터를 생성한다") {
                // given
                val coupon: Coupon = mockk {
                    every { id } returns 1
                    every { totalIssuedQuantity } returns 5_000
                    every { issuedStartedAt } returns LocalDateTime.now().withNano(0)
                }
                val memberId: Long = 1

                // when
                val result: Long = couponRedisRepository.createCouponIssued(
                    coupon = coupon,
                    memberId = memberId,
                )

                // then
                result.shouldBe(CouponIssued.READY)
            }

            context("totalIssuedQuantity 값을 넘어간 경우") {
                it("CouponIssued.EXIT 값을 반환한다") {
                    // given
                    val coupon: Coupon = mockk {
                        every { id } returns 1
                        every { totalIssuedQuantity } returns 1
                        every { issuedStartedAt } returns LocalDateTime.now().withNano(0)
                    }

                    // when
                    val firstResult: Long = couponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = 1,
                    )
                    val secondResult: Long = couponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = 2,
                    )

                    // then
                    firstResult.shouldBe(CouponIssued.READY)
                    secondResult.shouldBe(CouponIssued.EXIT)
                }
            }

            context("이미 발급된 데이터가 존재하는 경우") {
                it("CouponIssued.COMPLETE 값을 반환한다") {
                    // given
                    val coupon: Coupon = mockk {
                        every { id } returns 1
                        every { totalIssuedQuantity } returns 2_000
                        every { issuedStartedAt } returns LocalDateTime.now().withNano(0)
                    }
                    val memberId: Long = 1

                    // when
                    val firstResult: Long = couponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = memberId,
                    )
                    val secondResult: Long = couponRedisRepository.createCouponIssued(
                        coupon = coupon,
                        memberId = memberId,
                    )

                    // then
                    firstResult.shouldBe(CouponIssued.READY)
                    secondResult.shouldBe(CouponIssued.COMPLETE)
                }
            }
        }
    }
}

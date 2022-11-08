package com.hyoseok.repository

import com.hyoseok.config.RedisConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.RedisTemplateConfig
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.entity.CouponIssuedStatus.COMPLETE
import com.hyoseok.coupon.entity.CouponIssuedStatus.EXIT
import com.hyoseok.coupon.entity.CouponIssuedStatus.READY
import com.hyoseok.coupon.repository.CouponIssuedRedisRepository
import com.hyoseok.coupon.repository.CouponIssuedRedisRepositoryImpl
import com.hyoseok.coupon.repository.CouponIssuedRedisTransactionRepository
import com.hyoseok.coupon.repository.CouponIssuedRedisTransactionRepositoryImpl
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        RedisConfig::class,
        RedisTemplateConfig::class,
        CouponIssuedRedisRepositoryImpl::class,
        CouponIssuedRedisTransactionRepositoryImpl::class,
        CouponIssuedRedisRepository::class,
        CouponIssuedRedisTransactionRepository::class,
    ],
)
internal class CouponIssuedRedisTransactionRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var couponIssuedRedisTransactionRepository: CouponIssuedRedisTransactionRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("createCouponIssued 메서드는") {
            it("트랜잭션 기반으로 쿠폰 발급 데이터를 생성한다") {
                // given
                val couponIssued = CouponIssued(
                    couponId = 1,
                    totalIssuedQuantity = 5_000,
                    issuedDate = LocalDate.now(),
                )
                val memberId = 1L

                // when
                val result: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
                    couponIssued = couponIssued,
                    memberId = memberId,
                )

                // then
                result.shouldBe(READY.code)
            }

            context("totalIssuedQuantity 값을 넘어간 경우") {
                it("CouponIssued.EXIT 값을 반환한다") {
                    // given
                    val couponIssued = CouponIssued(
                        couponId = 1,
                        totalIssuedQuantity = 1,
                        issuedDate = LocalDate.now(),
                    )
                    val memberId = 1L

                    // when
                    val firstResult: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
                        couponIssued = couponIssued,
                        memberId = memberId,
                    )
                    val secondResult: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
                        couponIssued = couponIssued,
                        memberId = memberId,
                    )

                    // then
                    firstResult.shouldBe(READY.code)
                    secondResult.shouldBe(EXIT.code)
                }
            }

            context("이미 발급된 데이터가 존재하는 경우") {
                it("CouponIssued.COMPLETE 값을 반환한다") {
                    // given
                    val couponIssued = CouponIssued(
                        couponId = 1,
                        totalIssuedQuantity = 2_000,
                        issuedDate = LocalDate.now(),
                    )
                    val memberId = 1L

                    // when
                    val firstResult: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
                        couponIssued = couponIssued,
                        memberId = memberId,
                    )
                    val secondResult: Long = couponIssuedRedisTransactionRepository.createCouponIssued(
                        couponIssued = couponIssued,
                        memberId = memberId,
                    )

                    // then
                    firstResult.shouldBe(READY.code)
                    secondResult.shouldBe(COMPLETE.code)
                }
            }
        }
    }
}

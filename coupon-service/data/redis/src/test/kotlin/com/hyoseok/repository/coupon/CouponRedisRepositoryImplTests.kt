package com.hyoseok.repository.coupon

import com.hyoseok.config.RedisKey
import com.hyoseok.config.coupon.RedisCouponConfig
import com.hyoseok.config.coupon.RedisCouponEmbbededServerConfig
import com.hyoseok.config.coupon.RedisCouponTemplateConfig
import com.hyoseok.coupon.entity.CouponIssued
import com.hyoseok.coupon.repository.CouponRedisRepository
import com.hyoseok.repository.coupon.CouponRedisRepositoryImpl
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate

@DataRedisTest
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

    init {
        this.describe("executeUsingTransaction 메서드는") {
            it("트랜잭션 기반으로 Redis 명령어들을 수행되며, 결과를 반환한다") {
                // given
                val coupontId: Long = 1
                val issuedDate: LocalDate = LocalDate.now()
                val key: String = RedisKey.getCouponIssuedKey(couponId = coupontId, issuedDate = issuedDate)
                val memberId: Long = 1

                // when
                val result: Long? = couponRedisRepository.executeUsingTransaction {
                    couponRedisRepository.scard(key = key)
                    couponRedisRepository.sadd(key = key, value = memberId)
                }

                // then
                result.shouldNotBeNull()
                result.shouldBe(CouponIssued.READY)
            }
        }
    }
}

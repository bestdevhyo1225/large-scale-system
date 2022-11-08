package com.hyoseok.repository

import com.hyoseok.config.RedisConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.RedisTemplateConfig
import com.hyoseok.coupon.repository.CouponIssuedRedisRepository
import com.hyoseok.coupon.repository.CouponIssuedRedisRepositoryImpl
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

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        RedisConfig::class,
        RedisTemplateConfig::class,
        CouponIssuedRedisRepositoryImpl::class,
        CouponIssuedRedisRepository::class,
    ],
)
internal class CouponIssuedRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var couponIssuedRedisRepository: CouponIssuedRedisRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("sadd 메서드는") {
            it("key, value 값을 set 자료구조에 저장한다") {
                // given
                val key = "key"
                val memberId = 1L

                // when
                couponIssuedRedisRepository.sadd(key = key, value = memberId)

                // then
                couponIssuedRedisRepository.scard(key = key)
                    .shouldBe(expected = 1)
            }
        }
    }
}

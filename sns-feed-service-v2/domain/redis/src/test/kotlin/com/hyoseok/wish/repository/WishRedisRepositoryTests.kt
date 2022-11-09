package com.hyoseok.wish.repository

import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
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
        PostRedisConfig::class,
        PostRedisTemplateConfig::class,
        WishRedisRepositoryImpl::class,
        WishRedisRepository::class,
    ],
)
internal class WishRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var wishRedisRepository: WishRedisRepository

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
                wishRedisRepository.sadd(key = key, value = memberId)

                // then
                wishRedisRepository.scard(key = key)
                    .shouldBe(expected = 1)
            }
        }
    }
}

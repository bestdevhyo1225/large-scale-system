package com.hyoseok.repository

import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.standalone.ReactiveRedisStandaloneConfig
import com.hyoseok.config.standalone.ReactiveRedisStandaloneTemplateConfig
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
import java.time.Duration

@DataRedisTest
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        ReactiveRedisStandaloneConfig::class,
        ReactiveRedisStandaloneTemplateConfig::class,
        UrlReactiveRedisStandaloneRepository1::class,
        UrlReactiveRedisStandaloneRepository2::class,
        UrlReactiveRedisStandaloneRepository3::class,
        UrlReactiveRedisRepository::class,
    ],
)
internal class UrlReactiveRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var urlReactiveRedisRepository: UrlReactiveRedisRepository

    init {
        this.describe("set 메서드는") {
            it("여러 대의 Redis 서버에 데이터를 캐싱한다.") {
                // given
                val key = "key"
                val value = "value"
                val expireDuration = Duration.ofSeconds(60)

                // when
                urlReactiveRedisRepository.set(key = key, value = value, expireDuration = expireDuration)

                // then
                urlReactiveRedisRepository.get(key = key, clazz = String::class.java)
                    .shouldNotBeNull()
                    .shouldBe(value)
            }
        }
    }
}

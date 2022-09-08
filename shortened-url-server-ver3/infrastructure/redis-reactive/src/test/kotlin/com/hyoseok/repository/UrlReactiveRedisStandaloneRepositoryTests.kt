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
    ],
)
internal class UrlReactiveRedisStandaloneRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var urlReactiveRedisStandaloneRepository1: UrlReactiveRedisStandaloneRepository1

    @Autowired
    private lateinit var urlReactiveRedisStandaloneRepository2: UrlReactiveRedisStandaloneRepository2

    @Autowired
    private lateinit var urlMultipleReactiveRedisRepository3: UrlReactiveRedisStandaloneRepository3

    init {
        this.describe("set 메서드는") {
            it("캐시를 저장한다.") {
                // given
                val key = "key"
                val value = "value"
                val duration = Duration.ofSeconds(60)

                // when
                urlReactiveRedisStandaloneRepository1.set(key = key, value = value, duration = duration)
                urlReactiveRedisStandaloneRepository2.set(key = key, value = value, duration = duration)
                urlMultipleReactiveRedisRepository3.set(key = key, value = value, duration = duration)

                // then
                urlReactiveRedisStandaloneRepository1.get(key = key, clazz = String::class.java)
                    .shouldNotBeNull()
                    .shouldBe(value)

                urlReactiveRedisStandaloneRepository2.get(key = key, clazz = String::class.java)
                    .shouldNotBeNull()
                    .shouldBe(value)

                urlMultipleReactiveRedisRepository3.get(key = key, clazz = String::class.java)
                    .shouldNotBeNull()
                    .shouldBe(value)
            }
        }
    }
}

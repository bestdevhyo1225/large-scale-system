package com.hyoseok.repository

import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.RedisExpireTimes.SNS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.standalone.RedisStandaloneConfig
import com.hyoseok.config.standalone.RedisStandaloneTemplateConfig
import com.hyoseok.repository.sns.SnsCacheRepositoryImpl
import com.hyoseok.repository.sns.read.SnsCacheReadRepositoryImpl
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.entity.SnsImage
import com.hyoseok.sns.entity.SnsTag
import com.hyoseok.sns.entity.enums.SnsTagType
import com.hyoseok.sns.repository.SnsCacheRepository
import com.hyoseok.sns.repository.read.SnsCacheReadRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        RedisStandaloneConfig::class,
        RedisStandaloneTemplateConfig::class,
        SnsCacheRepository::class,
        SnsCacheReadRepository::class,
        SnsCacheRepositoryImpl::class,
        SnsCacheReadRepositoryImpl::class,
    ],
)
internal class SnsCacheRepositoryImplTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var snsCacheRepository: SnsCacheRepository

    @Autowired
    private lateinit var snsCacheReadRepository: SnsCacheReadRepository

    init {
        this.describe("setex 메서드는") {
            it("캐시를 저장한다") {
                // given
                val id = 1L
                val key = RedisKeys.getSnsKey(id = id)
                val snsCache = SnsCache(
                    id = id,
                    memberId = 1234L,
                    title = "title",
                    contents = "contents",
                    writer = "writer",
                    isDisplay = true,
                    createdAt = LocalDateTime.now().withNano(0),
                    updatedAt = LocalDateTime.now().withNano(0),
                    snsImages = listOf(SnsImage(id = 1L, url = "https://test.com", sortOrder = 0)),
                    snsTag = SnsTag(id = 1L, type = SnsTagType.STYLE, values = listOf("캐주얼", "출근")),
                    products = listOf(),
                )

                // when
                snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)

                // then
                val value = snsCacheReadRepository.get(key = key, clazz = SnsCache::class.java)

                value.shouldBe(snsCache)
            }
        }

        this.describe("zaddString 메서드는") {
            it("key, value, score를 저장한다") {
                // given
                val key = RedisKeys.SNS_ZSET_KEY
                val values = listOf(RedisKeys.getSnsKey(id = 1L), RedisKeys.getSnsKey(id = 2L))
                val nowDateTime = LocalDateTime.now().withNano(0)
                val scores = listOf(
                    Timestamp.valueOf(nowDateTime).time.toDouble(),
                    Timestamp.valueOf(nowDateTime.plusHours(1)).time.toDouble(),
                )

                // when
                snsCacheRepository.zaddString(key = key, value = values[0], score = scores[0])
                snsCacheRepository.zaddString(key = key, value = values[1], score = scores[1])

                // then
                snsCacheReadRepository.zrevrangeString(key = key, startIndex = 0, endIndex = 1)
                    .containsAll(values)
            }
        }

        this.describe("mget 메서드는") {
            it("한 번에 여러개의 캐시 데이터를 가져온다") {
                // given
                val ids = listOf(1L, 2L)
                val keys = ids.map { RedisKeys.getSnsKey(id = it) }
                val snsCaches = ids.map {
                    SnsCache(
                        id = it,
                        memberId = 1234L,
                        title = "title",
                        contents = "contents",
                        writer = "writer",
                        isDisplay = true,
                        createdAt = LocalDateTime.now().withNano(0),
                        updatedAt = LocalDateTime.now().withNano(0),
                        snsImages = listOf(SnsImage(id = 1L, url = "https://test.com", sortOrder = 0)),
                        snsTag = SnsTag(id = 1L, type = SnsTagType.STYLE, values = listOf("캐주얼", "출근")),
                        products = listOf(),
                    )
                }

                snsCaches.forEachIndexed { index, snsCache ->
                    snsCacheRepository.setex(key = keys[index], value = snsCache, expireTime = SNS, timeUnit = SECONDS)
                }

                // when
                val values = snsCacheReadRepository.mget(keys = keys, clazz = SnsCache::class.java)

                // then
                values.containsAll(snsCaches)
            }

            context("null 값이 존재하는 경우") {
                it("null 값을 제외한 리스트를 반환한다") {
                    // given
                    val ids = listOf(1L, 2L)
                    val keys = ids.map { RedisKeys.getSnsKey(id = it) }
                    val snsCache = SnsCache(
                        id = ids.first(),
                        memberId = 1234L,
                        title = "title",
                        contents = "contents",
                        writer = "writer",
                        isDisplay = true,
                        createdAt = LocalDateTime.now().withNano(0),
                        updatedAt = LocalDateTime.now().withNano(0),
                        snsImages = listOf(SnsImage(id = 1L, url = "https://test.com", sortOrder = 0)),
                        snsTag = SnsTag(id = 1L, type = SnsTagType.STYLE, values = listOf("캐주얼", "출근")),
                        products = listOf(),
                    )

                    snsCacheRepository.setex(key = keys.first(), value = snsCache, expireTime = SNS, timeUnit = SECONDS)

                    // when
                    val values = snsCacheReadRepository.mget(keys = keys, clazz = SnsCache::class.java)

                    // then
                    values.shouldHaveSize(ids.size.minus(1))
                }
            }

            context("빈 값의 keys인 경우") {
                it("빈 리스트를 반환한다") {
                    snsCacheReadRepository.mget(keys = listOf(), clazz = SnsCache::class.java).isEmpty()
                }
            }
        }

        this.describe("setAllEx 메서드는") {
            it("파이프라인을 활용해서 여러 캐시를 한 번에 저장한다") {
                val ids = listOf(1L, 2L)
                val keysAndValues: List<Pair<String, SnsCache>> = ids.map {
                    Pair(
                        first = RedisKeys.getSnsKey(id = it),
                        second = SnsCache(
                            id = it,
                            memberId = 1234L,
                            title = "title",
                            contents = "contents",
                            writer = "writer",
                            isDisplay = true,
                            createdAt = LocalDateTime.now().withNano(0),
                            updatedAt = LocalDateTime.now().withNano(0),
                            snsImages = listOf(SnsImage(id = 1L, url = "https://test.com", sortOrder = 0)),
                            snsTag = SnsTag(id = 1L, type = SnsTagType.STYLE, values = listOf("캐주얼", "출근")),
                            products = listOf(),
                        ),
                    )
                }

                // when
                snsCacheRepository.setAllEx(keysAndValues = keysAndValues, expireTime = SNS, timeUnit = SECONDS)

                // then
                snsCacheReadRepository.mget(keys = keysAndValues.map { it.first }, clazz = SnsCache::class.java)
                    .containsAll(keysAndValues.map { it.second })
            }
        }

        this.describe("zrevrangeString 메서드는") {
            context("key에 대한 값이 없는 경우") {
                it("빈 리스트를 반환한다") {
                    snsCacheReadRepository.zrevrangeString(key = "test", startIndex = 0, endIndex = 5)
                        .shouldBeEmpty()
                }
            }
        }
    }
}

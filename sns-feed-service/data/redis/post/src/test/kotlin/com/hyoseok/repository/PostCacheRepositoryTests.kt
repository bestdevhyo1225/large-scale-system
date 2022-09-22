package com.hyoseok.repository

import com.hyoseok.config.RedisPostExpireTimes.POST
import com.hyoseok.config.RedisPostExpireTimes.POST_VIEWS
import com.hyoseok.config.RedisPostKeys
import com.hyoseok.config.RedisPostKeys.POST_KEYS
import com.hyoseok.config.RedisPostConfig
import com.hyoseok.config.RedisPostEmbbededServerConfig
import com.hyoseok.config.RedisPostServerProperties
import com.hyoseok.config.RedisPostTemplateConfig
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostCacheReadRepository
import com.hyoseok.post.repository.PostCacheRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.RedisSystemException
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
        RedisPostServerProperties::class,
        RedisPostEmbbededServerConfig::class,
        RedisPostConfig::class,
        RedisPostTemplateConfig::class,
        PostCacheRepository::class,
        PostCacheReadRepository::class,
        PostCacheRepositoryImpl::class,
        PostCacheReadRepositoryImpl::class,
    ],
)
private class PostCacheRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var postCacheRepository: PostCacheRepository

    @Autowired
    private lateinit var postCacheReadRepository: PostCacheReadRepository

    init {
        this.describe("increment 메서드는") {
            it("key의 value를 1씩 증가 시킨다") {
                // given
                val id = 1L
                val key: String = RedisPostKeys.getPostViewsKey(id = id)
                val value = 0L

                postCacheRepository.set(key = key, value = value, expireTime = POST_VIEWS, timeUnit = SECONDS)

                // when
                val result: Long = postCacheRepository.increment(key = key)

                // then
                result.shouldBe(value.plus(1))
            }

            context("key가 존재하지 않는 경우") {
                it("value의 초기 값은 1이다") {
                    // given
                    val id = 1L
                    val key: String = RedisPostKeys.getPostViewsKey(id = id)

                    // when
                    val result: Long = postCacheRepository.increment(key = key)

                    // then
                    result.shouldBe(1)
                }
            }

            context("value가 정수형이 아닌 경우") {
                it("예외를 던진다") {
                    val id = 1L
                    val key: String = RedisPostKeys.getPostViewsKey(id = id)
                    val value = "testValue"

                    postCacheRepository.set(key = key, value = value, expireTime = POST_VIEWS, timeUnit = SECONDS)

                    shouldThrow<RedisSystemException> { postCacheRepository.increment(key = key) }
                }
            }
        }

        this.describe("set 메서드는") {
            it("PostCache 엔티티를 저장한다") {
                // given
                val id = 1L
                val key: String = RedisPostKeys.getPostKey(id = id)
                val snsCache = PostCache(
                    id = id,
                    memberId = 1234L,
                    title = "title",
                    contents = "contents",
                    writer = "writer",
                    createdAt = LocalDateTime.now().withNano(0),
                    updatedAt = LocalDateTime.now().withNano(0),
                    images = listOf(PostImage(id = 1L, url = "https://test.com", sortOrder = 0)),
                )

                // when
                postCacheRepository.set(key = key, value = snsCache, expireTime = POST, timeUnit = SECONDS)

                // then
                postCacheReadRepository.get(key = key, clazz = PostCache::class.java)
                    .shouldBe(snsCache)
            }
        }

        this.describe("zadd 메서드는") {
            it("key, value, score를 저장한다") {
                // given
                val key: String = POST_KEYS
                val values = listOf(RedisPostKeys.getPostKey(id = 1L), RedisPostKeys.getPostKey(id = 2L))
                val nowDateTime = LocalDateTime.now().withNano(0)
                val scores = listOf(
                    Timestamp.valueOf(nowDateTime).time.toDouble(),
                    Timestamp.valueOf(nowDateTime.plusHours(1)).time.toDouble(),
                )

                // when
                postCacheRepository.zadd(key = key, value = values[0], score = scores[0])
                postCacheRepository.zadd(key = key, value = values[1], score = scores[1])

                // then
                postCacheReadRepository.zrevrange(key = key, start = 0, end = 1, clazz = String::class.java)
                    .containsAll(values)
            }
        }
    }
}

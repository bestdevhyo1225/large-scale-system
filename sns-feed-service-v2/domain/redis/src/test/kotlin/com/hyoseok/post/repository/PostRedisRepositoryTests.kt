package com.hyoseok.post.repository

import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisEmbbededServerConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.POST_KEYS
import com.hyoseok.post.entity.PostImageCache
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
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
        PostRedisEmbbededServerConfig::class,
        PostRedisConfig::class,
        PostRedisTemplateConfig::class,
        PostRedisRepositoryImpl::class,
        PostRedisRepository::class,
    ],
)
internal class PostRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var postRedisRepository: PostRedisRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("set 메서드는") {
            it("key, value를 저장한다") {
                // given
                val postId: Long = 1
                val (key: String, expireTime: Long) = PostCache.getPostKeyAndExpireTime(id = postId)
                val value = PostCache(
                    id = postId,
                    memberId = 1234L,
                    title = "title",
                    contents = "contents",
                    writer = "writer",
                    createdAt = LocalDateTime.now().withNano(0),
                    updatedAt = LocalDateTime.now().withNano(0),
                    images = listOf(PostImageCache(id = 1L, url = "https://test.com", sortOrder = 0)),
                )

                // when
                postRedisRepository.set(key = key, value = value, expireTime = expireTime, timeUnit = SECONDS)

                // then
                postRedisRepository.get(key = key, clazz = PostCache::class.java)
                    .shouldBe(value)
            }
        }

        this.describe("zadd 메서드는") {
            it("key, value, score를 저장한다") {
                // given
                val key: String = POST_KEYS
                val postId: Long = 1
                val score: Double = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

                // when
                postRedisRepository.zadd(key = key, value = postId, score = score)

                // then
                postRedisRepository.zrevRange(key = key, start = 0, end = 1, clazz = Long::class.java)
                    .shouldContain(postId)
            }
        }

        this.describe("zremRangeByRank 메서드는") {
            it("start, end 범위만큼 value를 삭제한다") {
                // given
                val key: String = POST_KEYS
                val postIds: List<Long> = (1L..3L).map { it }
                val createdAt: LocalDateTime = LocalDateTime.now().withNano(0)
                val scores: List<Double> = (0L..2L).map {
                    Timestamp.valueOf(createdAt.plusDays(it)).time.toDouble()
                }
                val start: Long = -1
                val end: Long = -1

                postIds.forEachIndexed { index, value ->
                    postRedisRepository.zadd(key = key, value = value, score = scores[index])
                }

                // when
                postRedisRepository.zremRangeByRank(key = key, start = start, end = end)

                // then
                postRedisRepository.zrevRange(key = key, start = 0, end = 10, clazz = Long::class.java)
                    .shouldHaveSize(size = 2)
            }
        }

        this.describe("increment 메서드는") {
            it("정수형의 value를 1 증가 시킨다") {
                // given
                val postId: Long = 1
                val key: String = PostCache.getPostIdViewsKey(id = postId)

                // when
                postRedisRepository.increment(key = key)

                // then
                postRedisRepository.get(key = key, clazz = Long::class.java)
                    .shouldBe(expected = 1)
            }
        }
    }
}

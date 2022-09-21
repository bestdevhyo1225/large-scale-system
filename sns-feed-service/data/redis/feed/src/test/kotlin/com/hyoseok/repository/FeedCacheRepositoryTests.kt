package com.hyoseok.repository

import com.hyoseok.config.RedisFeedConfig
import com.hyoseok.config.RedisFeedEmbbededServerConfig
import com.hyoseok.config.RedisFeedServerProperties
import com.hyoseok.config.RedisFeedTemplateConfig
import com.hyoseok.config.RedisKeys
import com.hyoseok.feed.entity.FeedCache
import com.hyoseok.feed.repository.FeedCacheReadRepository
import com.hyoseok.feed.repository.FeedCacheRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.ints.shouldBeZero
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.MILLISECONDS

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisFeedServerProperties::class,
        RedisFeedEmbbededServerConfig::class,
        RedisFeedConfig::class,
        RedisFeedTemplateConfig::class,
        FeedCacheRepository::class,
        FeedCacheReadRepository::class,
        FeedCacheRepositoryImpl::class,
        FeedCacheReadRepositoryImpl::class,
    ],
)
internal class FeedCacheRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var feedCacheRepository: FeedCacheRepository

    @Autowired
    private lateinit var feedCacheReadRepository: FeedCacheReadRepository

    init {
        this.describe("zadd 메서드는") {
            it("key, value, score를 저장한다") {
                // given
                val postId = 1245L
                val memberId = 19283L
                val key = RedisKeys.getMemberFeedKey(id = memberId)
                val value = FeedCache(postId = postId)
                val score = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

                // when
                feedCacheRepository.zadd(key = key, value = value, score = score)

                // then
                val feedCaches: List<FeedCache> =
                    feedCacheReadRepository.zrevrange(key = key, start = 0, end = 1, clazz = FeedCache::class.java)

                feedCaches.size.shouldBe(1)
                feedCaches.first().shouldBe(value)
            }

            context("이미 ExpireTime 값이 지정되어 있는 경우에는") {
                it("데이터만 저장한다") {
                    // given
                    val memberId = 19283L
                    val key: String = RedisKeys.getMemberFeedKey(id = memberId)
                    val postIds: List<Long> = listOf(1245L, 1231L)
                    val values: List<FeedCache> = postIds.map { FeedCache(postId = it) }
                    val now: LocalDateTime = LocalDateTime.now().withNano(0)
                    val scores: List<Double> = listOf(
                        Timestamp.valueOf(now).time.toDouble(),
                        Timestamp.valueOf(now.plusMinutes(5)).time.toDouble(),
                    )

                    feedCacheRepository.zadd(key = key, value = values.first(), score = scores.first())
                    val firstExpireTime: Long = feedCacheReadRepository.getExpire(key = key, timeUnit = MILLISECONDS)

                    delay(timeMillis = 300)

                    // when
                    feedCacheRepository.zadd(key = key, value = values[1], score = scores[1])
                    val secondExpireTime: Long = feedCacheReadRepository.getExpire(key = key, timeUnit = MILLISECONDS)

                    // then
                    val feedCaches: List<FeedCache> =
                        feedCacheReadRepository.zrevrange(key = key, start = 0, end = 1, clazz = FeedCache::class.java)

                    feedCaches.size.shouldBe(2)
                    firstExpireTime.shouldBeGreaterThan(secondExpireTime)
                }
            }
        }

        this.describe("zremRangeByRank 메서드는") {
            it("start, end 범위에 포함된 데이터를 삭제한다") {
                // given
                val postId = 1245L
                val memberId = 19283L
                val key = RedisKeys.getMemberFeedKey(id = memberId)
                val value = FeedCache(postId = postId)
                val score = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

                feedCacheRepository.zadd(key = key, value = value, score = score)

                // when
                feedCacheRepository.zremRangeByRank(key = key, start = 0, end = 1)

                // then
                val feedCaches: List<FeedCache> =
                    feedCacheReadRepository.zrevrange(key = key, start = 0, end = 1, clazz = FeedCache::class.java)

                feedCaches.size.shouldBeZero()
            }
        }
    }
}

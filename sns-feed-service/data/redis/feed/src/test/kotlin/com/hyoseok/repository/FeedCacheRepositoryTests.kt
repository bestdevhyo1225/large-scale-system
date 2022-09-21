package com.hyoseok.repository

import com.hyoseok.config.RedisFeedConfig
import com.hyoseok.config.RedisFeedEmbbededServerConfig
import com.hyoseok.config.RedisFeedServerProperties
import com.hyoseok.config.RedisFeedTemplateConfig
import com.hyoseok.config.RedisFeedKeys
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
                val key = RedisFeedKeys.getMemberFeedKey(id = memberId)
                val createdAt = LocalDateTime.now().withNano(0)
                val value = FeedCache(postId = postId, createdAt = createdAt)
                val score = Timestamp.valueOf(createdAt).time.toDouble()

                // when
                feedCacheRepository.zadd(key = key, value = value, score = score)

                // then
                val feedCaches: List<FeedCache> =
                    feedCacheReadRepository.zrevrange(key = key, start = 0, end = 1, clazz = FeedCache::class.java)

                feedCaches.size.shouldBe(1)
                feedCaches.first().shouldBe(value)
            }
        }

        this.describe("zremRangeByRank 메서드는") {
            it("start, end 범위에 포함된 데이터를 삭제한다") {
                // given
                val postId = 1245L
                val memberId = 19283L
                val key = RedisFeedKeys.getMemberFeedKey(id = memberId)
                val createdAt = LocalDateTime.now().withNano(0)
                val value = FeedCache(postId = postId, createdAt = createdAt)
                val score = Timestamp.valueOf(createdAt).time.toDouble()

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

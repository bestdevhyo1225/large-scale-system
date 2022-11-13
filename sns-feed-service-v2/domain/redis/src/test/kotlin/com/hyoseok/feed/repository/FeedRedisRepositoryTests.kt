package com.hyoseok.feed.repository

import com.hyoseok.config.FeedRedisConfig
import com.hyoseok.config.FeedRedisTemplateConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.feed.entity.Feed
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.ints.shouldBeZero
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

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        FeedRedisConfig::class,
        FeedRedisTemplateConfig::class,
        FeedRedisRepositoryImpl::class,
        FeedRedisRepository::class,
    ],
)
internal class FeedRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("feedRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var feedRedisRepository: FeedRedisRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("zadd 메서드는") {
            it("key, value, score를 저장한다") {
                // given
                val postId = 1L
                val memberId = 1L
                val key: String = Feed.getMemberIdFeedsKey(id = memberId)
                val score: Double = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

                // when
                feedRedisRepository.zadd(key = key, value = postId, score = score)

                // then
                val feeds: List<Long> =
                    feedRedisRepository.zrevRange(key = key, start = 0, end = 1, clazz = Long::class.java)

                feeds.size.shouldBe(1)
                feeds.first().shouldBe(postId)
            }
        }

        this.describe("zremRangeByRank 메서드는") {
            it("start, end 범위에 포함된 데이터를 삭제한다") {
                // given
                val postId = 1245L
                val memberId = 19283L
                val key: String = Feed.getMemberIdFeedsKey(id = memberId)
                val score = Timestamp.valueOf(LocalDateTime.now().withNano(0)).time.toDouble()

                feedRedisRepository.zadd(key = key, value = postId, score = score)

                // when
                feedRedisRepository.zremRangeByRank(key = key, start = 0, end = 1)

                // then
                val feeds: List<Feed> =
                    feedRedisRepository.zrevRange(key = key, start = 0, end = 1, clazz = Feed::class.java)

                feeds.size.shouldBeZero()
            }
        }

        this.describe("zrevRange 메서드는") {
            it("start, end 범위에 포함된 데이터를 조회한다") {
                // given
                val memberId = 1L
                val key: String = Feed.getMemberIdFeedsKey(id = memberId)
                (1L..10L).forEachIndexed { index, postId ->
                    val createdAt: LocalDateTime = LocalDateTime.now().withNano(0).plusMinutes(index.toLong())
                    val score = Timestamp.valueOf(createdAt).time.toDouble()
                    feedRedisRepository.zadd(key = key, value = postId, score = score)
                }

                // when
                val postIds1: List<Long> =
                    feedRedisRepository.zrevRange(key = key, start = 0, end = 4, clazz = Long::class.java)

                val postIds2: List<Long> =
                    feedRedisRepository.zrevRange(key = key, start = 5, end = 9, clazz = Long::class.java)

                val postIds3: List<Long> =
                    feedRedisRepository.zrevRange(key = key, start = 10, end = 14, clazz = Long::class.java)

                // then
                postIds1.shouldHaveSize(size = 5)
                postIds2.shouldHaveSize(size = 5)
                postIds3.shouldHaveSize(size = 0)
            }
        }
    }
}

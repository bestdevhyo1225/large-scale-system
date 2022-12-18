package com.hyoseok.wish.repository

import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.WishRedisConfig
import com.hyoseok.config.WishRedisTemplateConfig
import com.hyoseok.wish.entity.WishCache
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldBeGreaterThan
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
        RedisEmbbededServerConfig::class,
        WishRedisConfig::class,
        WishRedisTemplateConfig::class,
        WishRedisRepositoryImpl::class,
        WishRedisRepository::class,
    ],
)
internal class WishRedisRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("wishRedisTemplate")
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

        this.describe("zaddAndExpire 메서드는") {
            it("ZSET 자료구조에 데이터를 추가하고, 만료시간을 부여한다") {
                // given
                val postId = 1L
                val memberId = 1L
                val wishCache = WishCache(postId = postId, memberId = memberId)
                val key: String = wishCache.getWishPostKey()
                val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()

                // when
                wishRedisRepository.zaddAndExpire(
                    key = key,
                    value = wishCache.memberId,
                    score = score,
                    expireTime = wishCache.expireTime,
                    timeUnit = SECONDS,
                )

                // then
                wishRedisRepository.zcard(key = key).shouldBe(1)
                redisTemplate.getExpire(key).shouldBeGreaterThan(0)
            }
        }

        this.describe("zrevRangeByScore(start, end) 메서드는") {
            it("minScore, maxScore 범위에서 start, end 만큼 데이터를 가져온다") {
                // given
                val postId = 1L
                val memberIds: List<Long> = (1L..10L).map { it }
                val wishCaches: List<WishCache> = memberIds.map {
                    WishCache(postId = postId, memberId = it)
                }

                wishCaches.forEach { wishCache ->
                    wishRedisRepository.zaddAndExpire(
                        key = wishCache.getWishPostKey(),
                        value = wishCache.memberId,
                        score = Timestamp.valueOf(LocalDateTime.now()).time.toDouble(),
                        expireTime = wishCache.expireTime,
                        timeUnit = SECONDS,
                    )
                }

                val minScore: Double = Timestamp.valueOf(LocalDateTime.now().minusDays(1)).time.toDouble()
                val maxScore: Double = Timestamp.valueOf(LocalDateTime.now().plusDays(1)).time.toDouble()
                val start: Long = 0
                val end: Long = 1

                // when
                val wishedMemberIds: List<Long> = wishRedisRepository.zrevRangeByScore(
                    key = WishCache.getWishPostKey(postId = postId),
                    minScore = minScore,
                    maxScore = maxScore,
                    start = start,
                    end = end,
                    clazz = Long::class.java,
                )

                // then
                wishedMemberIds.shouldNotBeEmpty()
                wishedMemberIds.size.shouldBe(1)
            }
        }

        this.describe("zrevRangeByScore 메서드는") {
            it("minScore, maxScore 범위의 데이터를 가져온다") {
                // given
                val postId = 1L
                val memberIds: List<Long> = (1L..10L).map { it }
                val wishCaches: List<WishCache> = memberIds.map {
                    WishCache(postId = postId, memberId = it)
                }

                wishCaches.forEach { wishCache ->
                    wishRedisRepository.zaddAndExpire(
                        key = wishCache.getWishPostKey(),
                        value = wishCache.memberId,
                        score = Timestamp.valueOf(LocalDateTime.now()).time.toDouble(),
                        expireTime = wishCache.expireTime,
                        timeUnit = SECONDS,
                    )
                }

                val minScore: Double = Timestamp.valueOf(LocalDateTime.now().minusDays(1)).time.toDouble()
                val maxScore: Double = Timestamp.valueOf(LocalDateTime.now().plusDays(1)).time.toDouble()

                // when
                val wishedMemberIds: List<Long> = wishRedisRepository.zrevRangeByScore(
                    key = WishCache.getWishPostKey(postId = postId),
                    minScore = minScore,
                    maxScore = maxScore,
                    clazz = Long::class.java,
                )

                // then
                wishedMemberIds.shouldNotBeEmpty()
                wishedMemberIds.size.shouldBe(wishCaches.size)
            }
        }

        this.describe("zremRangeByScore 메서드는") {
            it("minScore, maxScore 범위의 데이터를 삭제한다") {
                // given
                val postId = 1L
                val memberIds: List<Long> = (1L..10L).map { it }
                val wishCaches: List<WishCache> = memberIds.map {
                    WishCache(postId = postId, memberId = it)
                }

                wishCaches.forEach { wishCache ->
                    wishRedisRepository.zaddAndExpire(
                        key = wishCache.getWishPostKey(),
                        value = wishCache.memberId,
                        score = Timestamp.valueOf(LocalDateTime.now()).time.toDouble(),
                        expireTime = wishCache.expireTime,
                        timeUnit = SECONDS,
                    )
                }

                val minScore: Double = Timestamp.valueOf(LocalDateTime.now().minusDays(1)).time.toDouble()
                val maxScore: Double = Timestamp.valueOf(LocalDateTime.now().plusDays(1)).time.toDouble()

                // when
                wishRedisRepository.zremRangeByScore(
                    key = WishCache.getWishPostKey(postId = postId),
                    minScore = minScore,
                    maxScore = maxScore,
                )

                wishRedisRepository.zrevRangeByScore(
                    key = WishCache.getWishPostKey(postId = postId),
                    minScore = minScore,
                    maxScore = maxScore,
                    clazz = Long::class.java,
                ).shouldBeEmpty()
            }
        }
    }
}

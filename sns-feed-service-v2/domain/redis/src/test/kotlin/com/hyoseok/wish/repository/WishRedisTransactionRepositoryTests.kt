package com.hyoseok.wish.repository

import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.wish.entity.WishCache
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.longs.shouldBeGreaterThan
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
        WishRedisTransactionRepositoryImpl::class,
        WishRedisRepository::class,
        WishRedisTransactionRepository::class,
    ],
)
internal class WishRedisTransactionRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var wishRedisTransactionRepository: WishRedisTransactionRepository

    @Autowired
    private lateinit var wishRedisRepository: WishRedisRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("createWish 메서드는") {
            it("게시글에 대한 좋아요 캐시를 생성한다") {
                // given
                val wishCache = WishCache(postId = 1, memberId = 1)

                // when
                wishRedisTransactionRepository.createWish(wishCache = wishCache)

                // then
                wishRedisRepository.scard(key = wishCache.getKey())
                    .shouldBe(expected = 1)

                redisTemplate.getExpire(wishCache.getKey())
                    .shouldBeGreaterThan(x = 0)
            }
        }
    }
}

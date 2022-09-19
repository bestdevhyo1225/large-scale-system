package com.hyoseok.repository.post

import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.config.RedisExpireTimes.POST
import com.hyoseok.config.RedisExpireTimes.POST_VIEWS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.post.RedisPostConfig
import com.hyoseok.config.post.RedisPostTemplateConfig
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostCacheReadRepository
import com.hyoseok.post.repository.PostCacheRepository
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        RedisPostConfig::class,
        RedisPostTemplateConfig::class,
        PostCacheRepository::class,
        PostCacheReadRepository::class,
        PostCacheRepositoryImpl::class,
        PostCacheReadRepositoryImpl::class,
    ],
)
internal class PostCacheRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var postCacheRepository: PostCacheRepository

    @Autowired
    private lateinit var postCacheReadRepository: PostCacheReadRepository

    init {
        this.describe("set 메서드는") {
            it("PostCache 엔티티를 저장한다") {
                // given
                val id = 1L
                val key: String = RedisKeys.getPostKey(id = id)
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
    }
}

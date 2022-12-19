package com.hyoseok.post.repository

import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImageCache
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@DataRedisTest
@DirtiesContext
@EnableAutoConfiguration
@ContextConfiguration(
    classes = [
        RedisEmbbededServerConfig::class,
        PostRedisConfig::class,
        PostRedisTemplateConfig::class,
        PostRedisPipelineRepositoryImpl::class,
        PostRedisRepositoryImpl::class,
        PostRedisTransactionRepositoryImpl::class,
        PostRedisPipelineRepository::class,
        PostRedisRepository::class,
        PostRedisTransactionRepository::class,
    ],
)
internal class PostRedisPipelineRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var postRedisPipelineRepository: PostRedisPipelineRepository

    @Autowired
    private lateinit var postRedisTransactionRepository: PostRedisTransactionRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("getPostCaches 메서드는") {
            it("파이프라인을 활용해서 PostCache 리스트를 가져온다") {
                // given
                val postIds: List<Long> = (1L..10L).map { it }
                postIds.forEach {
                    val postCache = PostCache(
                        id = it,
                        memberId = 1L,
                        title = "title",
                        contents = "contents",
                        writer = "writer",
                        viewCount = 1,
                        wishCount = 1,
                        createdAt = LocalDateTime.now().withNano(0),
                        updatedAt = LocalDateTime.now().withNano(0),
                        images = listOf(PostImageCache(id = 1L, url = "url", sortOrder = 1)),
                    )
                    postRedisTransactionRepository.createPostCache(postCache = postCache)
                }

                // when
                val postCacheDtos: List<PostCacheDto> = postRedisPipelineRepository.getPostCaches(ids = postIds)

                // then
                postCacheDtos.shouldHaveSize(postIds.size)
            }
        }
    }
}

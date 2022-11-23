package com.hyoseok.post.repository

import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.config.RedisEmbbededServerConfig
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImageCache
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
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
        PostRedisTransactionRepositoryImpl::class,
        PostRedisRepositoryImpl::class,
        PostRedisRepository::class,
        PostRedisTransactionRepository::class,
    ],
)
internal class PostRedisTransactionRepositoryTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var postRedisTransactionRepository: PostRedisTransactionRepository

    @Autowired
    private lateinit var postRedisRepository: PostRedisRepository

    init {
        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("createPostCache 메서드는") {
            it("트랜잭션을 활용해서 PostCache, PostViewCount를 저장한다") {
                // given
                val postCache = PostCache(
                    id = 1231623,
                    memberId = 1,
                    title = "title",
                    contents = "contents",
                    writer = "writer",
                    createdAt = LocalDateTime.now().withNano(0),
                    updatedAt = LocalDateTime.now().withNano(0),
                    images = listOf(PostImageCache(id = 1L, url = "url", sortOrder = 1)),
                )
                val postViewCount: Long = 1

                // when
                postRedisTransactionRepository.createPostCache(postCache = postCache, postViewCount = postViewCount)

                // then
                postRedisRepository.hget(
                    key = PostCache.getPostBucketKey(id = postCache.id),
                    hashKey = postCache.id,
                    clazz = PostCache::class.java,
                ).shouldBe(postCache)

                postRedisRepository.hget(
                    key = PostCache.getPostViewBucketKey(id = postCache.id),
                    hashKey = postCache.id,
                    clazz = Long::class.java,
                ).shouldBe(postViewCount)

                val PostMemberIdBucket: StringBuilder? = postRedisRepository.hget(
                    key = PostCache.getPostMemberIdBucketKey(memberId = postCache.memberId),
                    hashKey = postCache.memberId,
                    clazz = StringBuilder::class.java,
                )


                PostMemberIdBucket.shouldNotBeNull()
                PostMemberIdBucket.split(",").first().toLong().shouldBe(postCache.id)
            }
        }
    }
}

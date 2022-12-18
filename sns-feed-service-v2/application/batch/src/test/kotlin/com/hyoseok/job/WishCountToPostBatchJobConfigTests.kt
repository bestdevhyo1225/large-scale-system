package com.hyoseok.job

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.BatchConfig
import com.hyoseok.config.WishRedisConfig
import com.hyoseok.config.WishRedisTemplateConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisRepository
import com.hyoseok.wish.repository.WishRedisRepositoryImpl
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@SpringBatchTest
@SpringBootTest(
    classes = [
        BatchConfig::class,
        BasicDataSourceConfig::class,
        JpaConfig::class,
        WishRedisConfig::class,
        WishRedisTemplateConfig::class,
        WishCountToPostBatchJobConfig::class,
        WishRedisRepositoryImpl::class,
        PostRepository::class,
    ],
)
internal class WishCountToPostBatchJobConfigTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    @Qualifier("wishRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var wishRedisRepository: WishRedisRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    init {
        this.beforeSpec {
            val title = "title"
            val contents = "contents"
            val writer = "writer"
            val postImages: List<PostImage> = listOf(
                PostImage(url = "test1", sortOrder = 1),
                PostImage(url = "test2", sortOrder = 2),
            )
            val posts: List<Post> = (1L..10L).map {
                Post(
                    memberId = it,
                    title = title,
                    contents = contents,
                    writer = writer,
                    postImages = postImages,
                )
            }

            withContext(Dispatchers.IO) {
                postRepository.saveAll(posts)
            }

            val memberId = 1L
            val wishCaches: List<WishCache> = (1L..10L).map { WishCache(postId = it, memberId = memberId) }
            val score: Double = Timestamp.valueOf(LocalDateTime.now()).time.toDouble()

            wishCaches.forEach { wishCache ->
                wishRedisRepository.zaddAndExpire(
                    key = wishCache.getWishPostKey(),
                    value = wishCache.memberId,
                    score = score,
                    expireTime = wishCache.expireTime,
                    timeUnit = SECONDS,
                )
            }
        }

        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("WishCountToPostBatchJobConfig 클래스는") {
            it("좋아요 캐시를 Post 엔티티의 wishCount에 반영한다") {
                // given

                // when
                val jobExecution: JobExecution = jobLauncherTestUtils.launchJob()

                // then
                jobExecution.status.shouldBe(BatchStatus.COMPLETED)
                jobExecution.exitStatus.shouldBe(ExitStatus.COMPLETED)

                val posts: List<Post> = withContext(Dispatchers.IO) {
                    postRepository.findAll()
                }

                posts.shouldNotBeEmpty()
                posts.forEach {
                    it.wishCount.shouldNotBeZero()
                    it.wishCount.shouldBe(1)
                }
            }
        }
    }
}

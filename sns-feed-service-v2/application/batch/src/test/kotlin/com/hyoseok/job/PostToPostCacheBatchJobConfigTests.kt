package com.hyoseok.job

import com.hyoseok.config.BasicDataSourceConfig
import com.hyoseok.config.BatchConfig
import com.hyoseok.config.PostRedisConfig
import com.hyoseok.config.PostRedisTemplateConfig
import com.hyoseok.config.jpa.JpaConfig
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostRedisPipelineRepository
import com.hyoseok.post.repository.PostRedisPipelineRepositoryImpl
import com.hyoseok.post.repository.PostRedisRepositoryImpl
import com.hyoseok.post.repository.PostRedisTransactionRepositoryImpl
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.post.service.PostRedisService
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBatchTest
@SpringBootTest(
    classes = [
        BatchConfig::class,
        BasicDataSourceConfig::class,
        JpaConfig::class,
        PostRedisConfig::class,
        PostRedisTemplateConfig::class,
        PostToPostCacheBatchJobConfig::class,
        PostRedisPipelineRepositoryImpl::class,
        PostRedisTransactionRepositoryImpl::class,
        PostRedisRepositoryImpl::class,
        PostRedisService::class,
        PostRepository::class,
    ],
)
internal class PostToPostCacheBatchJobConfigTests : DescribeSpec() {

    override fun extensions(): List<Extension> = listOf(SpringExtension)
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerLeaf

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    @Qualifier("postRedisTemplate")
    private lateinit var redisTemplate: RedisTemplate<String, String?>

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var postRedisPipelineRepository: PostRedisPipelineRepository

    private val postIds: MutableList<Long> = mutableListOf()

    init {
        this.beforeSpec {
            val title = "title"
            val contents = "contents"
            val writer = "writer"
            (1L..10L).forEach {
                val post = Post(
                    memberId = it,
                    title = title,
                    contents = contents,
                    writer = writer,
                    postImages = listOf(
                        PostImage(url = "test1", sortOrder = 1),
                        PostImage(url = "test2", sortOrder = 2),
                    ),
                )
                postRepository.save(post)
                postIds.add(post.id!!)
            }
        }

        this.afterSpec {
            redisTemplate.delete(redisTemplate.keys("*"))
        }

        this.describe("PostToPostCacheBatchJob 클래스는") {
            it("Post 엔티티를 PostCache 엔티티에 반영한다") {
                // given
                // when
                val jobExecution: JobExecution = jobLauncherTestUtils.launchJob()

                // then
                jobExecution.status.shouldBe(BatchStatus.COMPLETED)
                jobExecution.exitStatus.shouldBe(ExitStatus.COMPLETED)

                postRedisPipelineRepository.hgetPostCaches(ids = postIds)
                    .shouldNotBeEmpty()
                    .shouldHaveSize(postIds.size)
            }
        }
    }
}

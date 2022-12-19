package com.hyoseok.job

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.entity.Post
import com.hyoseok.post.service.PostRedisService
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory

@Configuration
class PostToPostCacheBatchJobConfig(
    @Value("\${spring.batch.chunk-size:1000}")
    private val chunkSize: Int,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val postRedisService: PostRedisService,
) {

    companion object {
        const val POST_TO_POST_CACHE = "PostToPostCache"
    }

    @Bean(name = ["${POST_TO_POST_CACHE}Job"])
    fun job(): Job =
        jobBuilderFactory.get("${POST_TO_POST_CACHE}Job")
            .preventRestart()
            .start(step())
            .build()

    @Bean(name = ["${POST_TO_POST_CACHE}Step"])
    fun step(): Step =
        stepBuilderFactory.get("${POST_TO_POST_CACHE}Step")
            .chunk<Post, PostCacheDto>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()

    @Bean(name = ["${POST_TO_POST_CACHE}Reader"])
    fun reader(): JpaPagingItemReader<Post> {
        val sql = """
            SELECT p
            FROM Post p
            INNER JOIN FETCH p.postImages
            WHERE p.createdAt BETWEEN :fromCreatedAt AND :toCreatedAt
            AND p.deletedAt IS NULL
            """
        val nowDateTime: LocalDateTime = LocalDateTime.now()
        val params: Map<String, Any> = mapOf(
            "fromCreatedAt" to nowDateTime.minusDays(5),
            "toCreatedAt" to nowDateTime.plusDays(1),
        )

        return JpaPagingItemReaderBuilder<Post>()
            .name("${POST_TO_POST_CACHE}Reader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString(sql)
            .parameterValues(params)
            .build()
    }

    @Bean(name = ["${POST_TO_POST_CACHE}Processor"])
    fun processor(): ItemProcessor<Post, PostCacheDto> =
        ItemProcessor { post ->
            with(receiver = post) {
                PostCacheDto(
                    id = id!!,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    wishCount = wishCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = postImages.map {
                        PostImageCacheDto(id = it.id!!, url = it.url, sortOrder = it.sortOrder)
                    },
                )
            }
        }

    @Bean(name = ["${POST_TO_POST_CACHE}Writer"])
    fun writer(): ItemWriter<PostCacheDto> =
        ItemWriter { postCacheDtos ->
            postCacheDtos.forEach { postCacheDto ->
                postRedisService.createOrUpdate(dto = postCacheDto)
            }
        }
}

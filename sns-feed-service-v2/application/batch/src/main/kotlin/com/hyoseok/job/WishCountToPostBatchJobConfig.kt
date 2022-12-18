package com.hyoseok.job

import com.hyoseok.post.entity.Post
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.wish.entity.WishCache
import com.hyoseok.wish.repository.WishRedisRepository
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
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.persistence.EntityManagerFactory

@Configuration
class WishCountToPostBatchJobConfig(
    @Value("\${spring.batch.chunk-size:1000}")
    private val chunkSize: Int,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val wishRedisRepository: WishRedisRepository,
    private val postRepository: PostRepository,
) {

    companion object {
        const val WISH_COUNT_TO_POST = "wishCountToPost"
    }

    @Bean(name = ["${WISH_COUNT_TO_POST}Job"])
    fun job(): Job =
        jobBuilderFactory.get("${WISH_COUNT_TO_POST}Job")
            .preventRestart()
            .start(step())
            .build()

    @Bean(name = ["${WISH_COUNT_TO_POST}Step"])
    fun step(): Step =
        stepBuilderFactory.get("${WISH_COUNT_TO_POST}Step")
            .chunk<Post, Post>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()

    @Bean(name = ["${WISH_COUNT_TO_POST}Reader"])
    fun reader(): JpaPagingItemReader<Post> =
        JpaPagingItemReaderBuilder<Post>()
            .name("${WISH_COUNT_TO_POST}Reader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString("SELECT p FROM Post p WHERE p.deletedAt IS NULL")
            .build()

    @Bean(name = ["${WISH_COUNT_TO_POST}Processor"])
    fun processor(): ItemProcessor<Post, Post> =
        ItemProcessor { post ->
            val key: String = WishCache.getWishPostKey(postId = post.id!!)
            val nowDateTime: LocalDateTime = LocalDateTime.now()
            val minScore: Double = Timestamp.valueOf(nowDateTime.minusMinutes(30)).time.toDouble()
            val maxScore: Double = Timestamp.valueOf(nowDateTime).time.toDouble()
            val wishedMemberIds: List<Long> = wishRedisRepository.zrevRangeByScore(
                key = key,
                minScore = minScore,
                maxScore = maxScore,
                clazz = Long::class.java,
            )
            val postWishCount: Long = wishedMemberIds.size.toLong()
            wishRedisRepository.zremRangeByScore(key = key, minScore = minScore, maxScore = maxScore)

            post.updateWishCount(value = postWishCount)
            post
        }

    @Bean(name = ["${WISH_COUNT_TO_POST}Writer"])
    fun writer(): ItemWriter<Post> =
        ItemWriter { posts ->
            postRepository.saveAll(posts)
        }
}

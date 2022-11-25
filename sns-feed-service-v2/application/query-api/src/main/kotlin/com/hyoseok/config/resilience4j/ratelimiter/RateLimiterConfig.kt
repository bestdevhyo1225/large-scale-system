package com.hyoseok.config.resilience4j.ratelimiter

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POSTS_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.property.FindPostTimelineUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.FindPostUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.FindPostsUsecaseProperty
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableConfigurationProperties(
    value = [
        FindPostsUsecaseProperty::class,
        FindPostTimelineUsecaseProperty::class,
        FindPostUsecaseProperty::class,
    ],
)
class RateLimiterConfig(
    private val findPostsUsecaseProperty: FindPostsUsecaseProperty,
    private val findPostTimelineUsecaseProperty: FindPostTimelineUsecaseProperty,
    private val findPostUsecaseProperty: FindPostUsecaseProperty,
) {

    object Name {
        const val FIND_POSTS_USECASE = "findPostsUsecase"
        const val FIND_POST_TIMELINE_USECASE = "findPostTimelineUsecase"
        const val FIND_POST_USECASE = "findPostUsecase"
    }

    @Bean
    fun findPostsUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(findPostsUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(findPostsUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(findPostsUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(FIND_POSTS_USECASE)
    }

    @Bean
    fun findPostTimelineUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(findPostTimelineUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(findPostTimelineUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(findPostTimelineUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(FIND_POST_TIMELINE_USECASE)
    }

    @Bean
    fun findPostUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(findPostUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(findPostUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(findPostUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(FIND_POST_USECASE)
    }
}

package com.hyoseok.config.resilience4j.ratelimiter

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.property.FindPostTimelineUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.FindPostUsecaseProperty
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
        FindPostTimelineUsecaseProperty::class,
        FindPostUsecaseProperty::class,
    ],
)
class RateLimiterConfig(
    private val findPostTimelineUsecaseProperty: FindPostTimelineUsecaseProperty,
    private val findPostUsecaseProperty: FindPostUsecaseProperty,
) {

    object Name {
        const val FIND_POST_TIMELINE_USECASE = "findPostTimelineUsecase"
        const val FIND_POST_USECASE = "findPostUsecase"
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

package com.hyoseok.config.resilience4j.ratelimiter

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.CREATE_FOLLOW_MEMBER_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.CREATE_POST_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.CREATE_WISH_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.UPDATE_MEMBER_INFLUENCER_USECASE
import com.hyoseok.config.resilience4j.ratelimiter.property.CreateFollowMemberUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.CreatePostUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.CreateWishUsecaseProperty
import com.hyoseok.config.resilience4j.ratelimiter.property.UpdateMemberInfluencerUsecaseProperty
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
        CreateFollowMemberUsecaseProperty::class,
        CreatePostUsecaseProperty::class,
        CreateWishUsecaseProperty::class,
        UpdateMemberInfluencerUsecaseProperty::class,
    ],
)
class RateLimiterConfig(
    private val createFollowMemberUsecaseProperty: CreateFollowMemberUsecaseProperty,
    private val createPostUsecaseProperty: CreatePostUsecaseProperty,
    private val createWishUsecaseProperty: CreateWishUsecaseProperty,
    private val updateMemberInfluencerUsecaseProperty: UpdateMemberInfluencerUsecaseProperty,
) {

    object Name {
        const val CREATE_FOLLOW_MEMBER_USECASE = "createFollowMemberUsecase"
        const val CREATE_POST_USECASE = "createPostUsecase"
        const val CREATE_WISH_USECASE = "createWishUsecase"
        const val UPDATE_MEMBER_INFLUENCER_USECASE = "updateMemberInfluencerUsecase"
    }

    @Bean
    fun createFollowMemberUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(createFollowMemberUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(createFollowMemberUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(createFollowMemberUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(CREATE_FOLLOW_MEMBER_USECASE)
    }

    @Bean
    fun createPostUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(createPostUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(createPostUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(createPostUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(CREATE_POST_USECASE)
    }

    @Bean
    fun createWishUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(createWishUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(createWishUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(createWishUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(CREATE_WISH_USECASE)
    }

    @Bean
    fun updateMemberInfluencerUsecaseRateLimiter(): RateLimiter {
        val rateLimiterConfig: RateLimiterConfig = RateLimiterConfig.custom()
            .limitForPeriod(updateMemberInfluencerUsecaseProperty.limitForPeriod)
            .limitRefreshPeriod(Duration.ofSeconds(updateMemberInfluencerUsecaseProperty.limitRefreshPeriod))
            .timeoutDuration(Duration.ofSeconds(updateMemberInfluencerUsecaseProperty.timeoutDuration))
            .build()

        return RateLimiterRegistry
            .of(rateLimiterConfig)
            .rateLimiter(UPDATE_MEMBER_INFLUENCER_USECASE)
    }
}

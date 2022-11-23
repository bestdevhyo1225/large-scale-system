package com.hyoseok.config.resilience4j.ratelimiter.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(value = "resilience4j.ratelimiter.usecase.find-posts")
data class FindPostsUsecaseProperty(
    val limitForPeriod: Int,
    val limitRefreshPeriod: Long,
    val timeoutDuration: Long,
)

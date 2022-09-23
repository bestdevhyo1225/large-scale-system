package com.hyoseok.config.feed

import com.hyoseok.config.RedisMode
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@Profile(value = ["dev", "prod"])
@ConfigurationProperties(prefix = "spring.data.redis.feed")
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["feed"], havingValue = "true")
data class RedisFeedServerProperties(
    val mode: RedisMode,
    val nodes: Map<String, List<String>>,
)

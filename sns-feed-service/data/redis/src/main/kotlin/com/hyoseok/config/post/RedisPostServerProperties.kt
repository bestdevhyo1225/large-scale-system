package com.hyoseok.config.post

import com.hyoseok.config.RedisMode
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@Profile(value = ["dev", "prod"])
@ConfigurationProperties(prefix = "spring.data.redis.post")
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["post"], havingValue = "true")
data class RedisPostServerProperties(
    val mode: RedisMode,
    val nodes: Map<String, List<String>>,
)

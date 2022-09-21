package com.hyoseok.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.data.redis.post")
@Profile(value = ["dev", "prod"])
data class RedisPostServerProperties(
    val mode: RedisMode,
    val nodes: Map<String, List<String>>,
)

package com.hyoseok.config.property

import com.hyoseok.config.RedisMode
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.data.redis")
@Profile(value = ["dev", "prod"])
data class RedisServerProperties(
    val mode: RedisMode,
    val nodes: Map<String, List<String>>,
)

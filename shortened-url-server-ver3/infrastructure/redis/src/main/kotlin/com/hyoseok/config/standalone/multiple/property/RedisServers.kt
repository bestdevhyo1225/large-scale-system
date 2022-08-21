package com.hyoseok.config.standalone.multiple.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.data.redis")
@Profile(value = ["dev", "prod"])
data class RedisServers(
    val nodes: Map<String, List<String>>
)

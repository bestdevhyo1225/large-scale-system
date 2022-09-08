package com.hyoseok.config.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
data class RedisClusterServers(
    val nodes: Map<String, List<String>>,
)

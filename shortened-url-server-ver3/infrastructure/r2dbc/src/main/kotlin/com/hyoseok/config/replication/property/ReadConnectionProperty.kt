package com.hyoseok.config.replication.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.r2dbc.pool.read")
@Profile("prod")
data class ReadConnectionProperty(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String,
    val initialPoolSize: Int,
    val maxPoolSize: Int,
    val maxLifeTime: Long,
    val maxAcquireTime: Long,
)

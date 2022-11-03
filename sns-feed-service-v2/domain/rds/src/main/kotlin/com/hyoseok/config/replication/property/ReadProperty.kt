package com.hyoseok.config.replication.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(value = "spring.datasource.hikari.read")
@Profile(value = ["production"])
data class ReadProperty(
    val driverClassName: String,
    val jdbcUrl: String,
    val minimumIdle: Int,
    val maximumPoolSize: Int,
    val maxLifetime: Long,
    val connectionTimeout: Long,
)

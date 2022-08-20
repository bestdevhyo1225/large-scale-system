package com.hyoseok.config.mysql.replication.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(value = "spring.datasource.hikari.write")
@Profile(value = ["prod"])
data class WriteProperty(
    val driverClassName: String,
    val jdbcUrl: String,
    val minimumIdle: Int,
    val maximumPoolSize: Int,
    val maxLifetime: Long,
    val connectionTimeout: Long,
)

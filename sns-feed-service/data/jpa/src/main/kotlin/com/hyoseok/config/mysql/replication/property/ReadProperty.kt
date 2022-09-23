package com.hyoseok.config.mysql.replication.property

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile

@ConstructorBinding
@ConfigurationProperties(value = "data.datasource.hikari.read")
@Profile(value = ["prod"])
@ConditionalOnProperty(prefix = "data.enable", name = ["datasource"], havingValue = "true")
data class ReadProperty(
    val driverClassName: String,
    val jdbcUrl: String,
    val minimumIdle: Int,
    val maximumPoolSize: Int,
    val maxLifetime: Long,
    val connectionTimeout: Long,
)

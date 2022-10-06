package com.hyoseok.config.datasource

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties
@Profile(value = ["test", "dev"])
@ConditionalOnProperty(prefix = "data.enable", name = ["datasource"], havingValue = "true")
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "data.datasource.hikari")
    fun dataSource(): DataSource {
        return DataSourceBuilder.create()
            .type(HikariDataSource::class.java)
            .build()
    }
}

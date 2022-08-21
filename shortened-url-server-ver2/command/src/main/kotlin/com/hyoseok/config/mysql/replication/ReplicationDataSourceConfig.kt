package com.hyoseok.config.mysql.replication

import com.hyoseok.config.mysql.replication.property.ReadProperty
import com.hyoseok.config.mysql.replication.property.WriteProperty
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(value = [WriteProperty::class, ReadProperty::class])
@Profile(value = ["prod", "prod-docker"])
class ReplicationDataSourceConfig(
    private val writeProperty: WriteProperty,
    private val readProperty: ReadProperty,
) {

    @Bean
    fun writeDataSource(): DataSource = HikariDataSource().apply {
        poolName = "WriteDataSourcePool"
        driverClassName = writeProperty.driverClassName
        jdbcUrl = writeProperty.jdbcUrl
        minimumIdle = writeProperty.minimumIdle
        maximumPoolSize = writeProperty.maximumPoolSize
        maxLifetime = writeProperty.maxLifetime
        connectionTimeout = writeProperty.connectionTimeout
    }

    @Bean
    fun readDataSource(): DataSource = HikariDataSource().apply {
        poolName = "ReadDataSourcePool"
        driverClassName = readProperty.driverClassName
        jdbcUrl = readProperty.jdbcUrl
        minimumIdle = readProperty.minimumIdle
        maximumPoolSize = readProperty.maximumPoolSize
        maxLifetime = readProperty.maxLifetime
        connectionTimeout = readProperty.connectionTimeout
    }

    @Bean
    fun routingDataSource(): DataSource {
        val dataSourceMap: MutableMap<Any, Any> = HashMap()
        val writeDataSource = writeDataSource()
        val readDataSource = readDataSource()

        dataSourceMap[ReplicationDataSourceType.WRITE] = writeDataSource
        dataSourceMap[ReplicationDataSourceType.READ] = readDataSource

        return ReplicationRoutingDataSource().apply {
            setTargetDataSources(dataSourceMap)
            setDefaultTargetDataSource(writeDataSource)
        }
    }

    @Bean
    @Primary
    fun dataSource(): DataSource {
        return LazyConnectionDataSourceProxy(routingDataSource())
    }
}

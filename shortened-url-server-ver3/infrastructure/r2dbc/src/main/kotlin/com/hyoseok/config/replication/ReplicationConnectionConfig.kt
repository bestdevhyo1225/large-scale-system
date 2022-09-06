package com.hyoseok.config.replication

import com.hyoseok.config.R2DBCDriver
import com.hyoseok.config.replication.ReplicationRoutingConnectionKey.READ
import com.hyoseok.config.replication.ReplicationRoutingConnectionKey.WRITE
import com.hyoseok.config.replication.property.ReadConnectionProperty
import com.hyoseok.config.replication.property.WriteConnectionProperty
import io.r2dbc.pool.PoolingConnectionFactoryProvider.INITIAL_SIZE
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_ACQUIRE_TIME
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_LIFE_TIME
import io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import java.time.Duration

@Configuration
@EnableConfigurationProperties(value = [WriteConnectionProperty::class, ReadConnectionProperty::class])
@Profile("prod")
class ReplicationConnectionConfig(
    @Value("\${spring.r2dbc.pool.protocol}")
    private val protocol: String,
    private val writeConnectionProperty: WriteConnectionProperty,
    private val readConnectionProperty: ReadConnectionProperty,
) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        val connectionFactoriesMap: MutableMap<Any, ConnectionFactory> = HashMap()
        val writeConnectionFactory = writeConnectionFactory()
        val readConnectionFactory = readConnectionFactory()

        connectionFactoriesMap[WRITE] = writeConnectionFactory
        connectionFactoriesMap[READ] = readConnectionFactory

        return ReplicationRoutingConnectionFactory().apply {
            setDefaultTargetConnectionFactory(writeConnectionFactory)
            setTargetConnectionFactories(connectionFactoriesMap)
        }
    }

    @Bean
    fun writeConnectionFactory(): ConnectionFactory =
        createConnectionFactory(replicationRoutingConnectionKey = WRITE)

    @Bean
    fun readConnectionFactory(): ConnectionFactory =
        createConnectionFactory(replicationRoutingConnectionKey = READ)

    private fun createConnectionFactory(
        replicationRoutingConnectionKey: ReplicationRoutingConnectionKey,
    ): ConnectionFactory {
        val connectionFactoryOptions = ConnectionFactoryOptions.builder()
            .option(DRIVER, R2DBCDriver.POOL)
            .option(PROTOCOL, protocol)

        when (replicationRoutingConnectionKey) {
            WRITE -> setWriteConnectionOptions(connectionFactoryOptions = connectionFactoryOptions)
            READ -> setReadConnectionOptions(connectionFactoryOptions = connectionFactoryOptions)
        }

        return ConnectionFactories.get(connectionFactoryOptions.build())
    }

    private fun setWriteConnectionOptions(connectionFactoryOptions: ConnectionFactoryOptions.Builder) {
        with(receiver = writeConnectionProperty) {
            connectionFactoryOptions
                .option(HOST, host)
                .option(USER, user)
                .option(PORT, port)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .option(INITIAL_SIZE, initialPoolSize)
                .option(MAX_SIZE, maxPoolSize)
                .option(MAX_LIFE_TIME, Duration.ofMillis(maxLifeTime))
                .option(MAX_ACQUIRE_TIME, Duration.ofMillis(maxAcquireTime))
        }
    }

    private fun setReadConnectionOptions(connectionFactoryOptions: ConnectionFactoryOptions.Builder) {
        with(receiver = readConnectionProperty) {
            connectionFactoryOptions
                .option(HOST, host)
                .option(USER, user)
                .option(PORT, port)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .option(INITIAL_SIZE, initialPoolSize)
                .option(MAX_SIZE, maxPoolSize)
                .option(MAX_LIFE_TIME, Duration.ofMillis(maxLifeTime))
                .option(MAX_ACQUIRE_TIME, Duration.ofMillis(maxAcquireTime))
        }
    }
}

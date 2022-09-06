package com.hyoseok.config.r2dbc

import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.r2dbc.connection.R2dbcTransactionManager

@Configuration
@Profile("prod")
class R2DBCConfig {

    @Bean
    fun transactionManager(@Qualifier("writeConnectionFactory") connectionFactory: ConnectionFactory) =
        R2dbcTransactionManager(connectionFactory)
}

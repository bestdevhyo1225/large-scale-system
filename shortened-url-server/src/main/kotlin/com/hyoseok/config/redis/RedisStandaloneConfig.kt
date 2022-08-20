package com.hyoseok.config.redis

import io.lettuce.core.ClientOptions
import io.lettuce.core.ReadFrom.REPLICA_PREFERRED
import io.lettuce.core.TimeoutOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import java.time.Duration

@Configuration
@EnableCaching(proxyTargetClass = true)
@Profile(value = ["dev", "prod"])
class RedisStandaloneConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    @Value("\${spring.data.redis.command-timeout}")
    private val commandTimeout: Long,

    @Value("\${spring.data.redis.shutdown-timeout}")
    private val shutdownTimeout: Long,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory =
        LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())

    private fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("large-scale-system-client")
            .clientOptions(clientOptions())
            .readFrom(REPLICA_PREFERRED)
            .shutdownTimeout(Duration.ofMillis(shutdownTimeout))
            .build()

    private fun clientOptions(): ClientOptions =
        ClientOptions.builder()
            .timeoutOptions(timeoutOptions())
            .build()

    private fun timeoutOptions(): TimeoutOptions =
        TimeoutOptions.builder()
            .timeoutCommands()
            .fixedTimeout(Duration.ofMillis(commandTimeout))
            .build()
}

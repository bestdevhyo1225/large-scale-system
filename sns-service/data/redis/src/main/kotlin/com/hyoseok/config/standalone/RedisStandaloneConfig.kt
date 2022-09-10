package com.hyoseok.config.standalone

import io.lettuce.core.ReadFrom
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@EnableCaching(proxyTargetClass = true)
@Profile(value = ["test", "dev", "prod"])
class RedisStandaloneConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig()).apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, EagerInitialization 를 true로 처리하여 해결할 수 있다.
            eagerInitialization = true
        }
    }

    private fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("large-scale-system-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()
}

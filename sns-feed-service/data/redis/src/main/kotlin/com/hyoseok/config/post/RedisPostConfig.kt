package com.hyoseok.config.post

import com.hyoseok.config.RedisMode.Cluster
import com.hyoseok.config.RedisMode.Standalone
import io.lettuce.core.ReadFrom
import mu.KotlinLogging
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(value = [RedisPostServerProperties::class])
class RedisPostConfig(
    private val redisPostServerProperties: RedisPostServerProperties,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun redisPostConnectionFactory(): RedisConnectionFactory {
        val lettuceConnectionFactory: LettuceConnectionFactory = when (redisPostServerProperties.mode) {
            Standalone -> {
                logger.info { "redis post standalone mode" }

                val (host: String, port: Int) = getHostAndPort()
                LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())
            }

            Cluster -> {
                logger.info { "redis post cluster mode" }

                LettuceConnectionFactory(
                    RedisClusterConfiguration(redisPostServerProperties.nodes.values.first()),
                    lettuceClientConfig(),
                )
            }
        }

        return lettuceConnectionFactory.apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, EagerInitialization 를 true로 처리하여 해결할 수 있다.
            eagerInitialization = true
        }
    }

    private fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("redis-post-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()

    private fun getHostAndPort(): Pair<String, Int> {
        val splits: List<String> = redisPostServerProperties.nodes.values.first().first().split(":")
        return Pair(first = splits[0], second = splits[1].toInt())
    }
}

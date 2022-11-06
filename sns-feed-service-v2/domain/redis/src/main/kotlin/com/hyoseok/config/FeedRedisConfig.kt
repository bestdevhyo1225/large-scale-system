package com.hyoseok.config

import com.hyoseok.config.RedisMode.Cluster
import com.hyoseok.config.RedisMode.Standalone
import io.lettuce.core.ReadFrom
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisConfig(
    @Value("\${spring.feed.redis.mode}")
    private val mode: RedisMode,
    @Value("\${spring.feed.redis.nodes}")
    private val nodes: List<String>,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun feedRedisConnectionFactory(): RedisConnectionFactory {
        val lettuceConnectionFactory: LettuceConnectionFactory = when (mode) {
            Standalone -> {
                logger.info { "feed redis standalone mode" }

                val (host: String, port: Int) = getHostAndPort()
                LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())
            }

            Cluster -> {
                logger.info { "feed redis cluster mode" }

                LettuceConnectionFactory(RedisClusterConfiguration(nodes), lettuceClientConfig())
            }
        }

        return lettuceConnectionFactory.apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, EagerInitialization 를 true로 처리하여 해결할 수 있다.
            eagerInitialization = true
        }
    }

    private fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("feed-redis-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()

    private fun getHostAndPort(): Pair<String, Int> {
        val splits: List<String> = nodes.first().split(":")
        return Pair(first = splits[0], second = splits[1].toInt())
    }
}

package com.hyoseok.config.standalone.multiple

import com.hyoseok.config.RedisNodesKey.SERVER_1
import com.hyoseok.config.RedisNodesKey.SERVER_2
import com.hyoseok.config.RedisNodesKey.SERVER_3
import com.hyoseok.config.standalone.multiple.property.RedisServers
import io.lettuce.core.ReadFrom
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(value = [RedisServers::class])
@Profile(value = ["dev", "prod"])
class RedisMultipleStandaloneConfig(
    private val redisServers: RedisServers,
) {

    @Bean
    @Primary
    fun redisMultipleConnectionFactory1(): RedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_1)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())
    }

    @Bean
    fun redisMultipleConnectionFactory2(): RedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_2)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())
    }

    @Bean
    fun redisMultipleConnectionFactory3(): RedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_3)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig())
    }

    private fun getHostAndPort(redisNodesKey: String): Pair<String, Int> {
        val splitNodes = when (redisNodesKey) {
            SERVER_1 -> redisServers.nodes[SERVER_1]!!.first().split(":")
            SERVER_2 -> redisServers.nodes[SERVER_2]!!.first().split(":")
            SERVER_3 -> redisServers.nodes[SERVER_3]!!.first().split(":")
            else -> throw IllegalArgumentException("redisNodesKey 값을 다시 확인하세요")
        }

        return with(receiver = splitNodes) { Pair(first = this[0], second = this[1].toInt()) }
    }

    private fun lettuceClientConfig(): LettuceClientConfiguration =
        LettuceClientConfiguration.builder()
            .clientName("large-scale-system-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .build()
}

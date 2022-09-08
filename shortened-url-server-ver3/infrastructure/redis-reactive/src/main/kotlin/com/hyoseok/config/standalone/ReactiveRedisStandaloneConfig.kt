package com.hyoseok.config.standalone

import com.hyoseok.config.RedisNodesKey.SERVER_1
import com.hyoseok.config.RedisNodesKey.SERVER_2
import com.hyoseok.config.RedisNodesKey.SERVER_3
import com.hyoseok.config.standalone.property.RedisServers
import io.lettuce.core.ReadFrom
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(value = [RedisServers::class])
class ReactiveRedisStandaloneConfig(
    private val redisServers: RedisServers,
) {

    @Bean
    @Primary
    fun reactiveRedisConnectionFactory1(): ReactiveRedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_1)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig()).apply {
            // [ 공유 연결 -> shareNativeConnection ]
            // - shareNativeConnection 값을 true로 설정
            // - 여러 LettuceConnections 가 단일 기본 연결을 공유할 수 있도록 한다. false로 설정하면, LettuceConnection 의 모든 작업이 소켓을 열고 닫는다.
            // RedisClient.connect 에서 블록킹이 발생하는데, eagerInitialization 값을 true 로 설정하면 shareNativeConnection 의 즉시 초기화를 활성화한다.
            eagerInitialization = true
        }
    }

    @Bean
    fun reactiveRedisConnectionFactory2(): ReactiveRedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_2)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig()).apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, eagerInitialization 값을 true 로 설정하면 shareNativeConnection 의 즉시 초기화를 활성화한다.
            eagerInitialization = true
        }
    }

    @Bean
    fun reactiveRedisConnectionFactory3(): ReactiveRedisConnectionFactory {
        val (host, port) = getHostAndPort(redisNodesKey = SERVER_3)
        return LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettuceClientConfig()).apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, eagerInitialization 값을 true 로 설정하면 shareNativeConnection 의 즉시 초기화를 활성화한다.
            eagerInitialization = true
        }
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

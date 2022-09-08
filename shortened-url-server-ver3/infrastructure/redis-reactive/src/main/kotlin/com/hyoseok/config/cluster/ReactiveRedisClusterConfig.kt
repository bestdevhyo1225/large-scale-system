package com.hyoseok.config.cluster

import com.hyoseok.config.RedisCommonConfig
import com.hyoseok.config.RedisNodesKey.SERVER_1
import com.hyoseok.config.property.RedisClusterServers
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

//@Configuration
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(value = [RedisClusterServers::class])
class ReactiveRedisClusterConfig(
    private val redisClusterServers: RedisClusterServers,
) {

//    @Bean
    fun reactiveRedisClusterConnectionFactory1(): ReactiveRedisConnectionFactory {
        return LettuceConnectionFactory(
            RedisClusterConfiguration(redisClusterServers.nodes[SERVER_1]!!),
            RedisCommonConfig.lettuceClientConfig(),
        ).apply {
            // [ 공유 연결 -> shareNativeConnection ]
            // - shareNativeConnection 값을 true로 설정
            // - 여러 LettuceConnections 가 단일 기본 연결을 공유할 수 있도록 한다. false로 설정하면, LettuceConnection 의 모든 작업이 소켓을 열고 닫는다.
            // RedisClient.connect 에서 블록킹이 발생하는데, eagerInitialization 값을 true 로 설정하면 shareNativeConnection 의 즉시 초기화를 활성화한다.
            eagerInitialization = true
        }
    }
}

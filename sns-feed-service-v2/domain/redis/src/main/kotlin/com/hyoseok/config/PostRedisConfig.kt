package com.hyoseok.config

import com.hyoseok.config.RedisMode.Cluster
import com.hyoseok.config.RedisMode.Standalone
import io.lettuce.core.ReadFrom
import mu.KotlinLogging
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import java.time.Duration

@Configuration
@EnableCaching(proxyTargetClass = true)
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisConfig(
    @Value("\${spring.post.redis.mode}")
    private val mode: RedisMode,

    @Value("\${spring.post.redis.nodes}")
    private val nodes: List<String>,

    @Value("\${spring.feed.redis.lettuce.pool.max-active}")
    private val maxActive: Int,

    @Value("\${spring.feed.redis.lettuce.pool.max-idle}")
    private val maxIdle: Int,

    @Value("\${spring.feed.redis.lettuce.pool.max-idle}")
    private val minIdle: Int,

    @Value("\${spring.feed.redis.lettuce.pool.max-wait}")
    private val maxWait: Long,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    @Primary
    fun postRedisConnectionFactory(): RedisConnectionFactory {
        val lettuceConnectionFactory: LettuceConnectionFactory = when (mode) {
            Standalone -> {
                logger.info { "post redis standalone mode" }

                val (host: String, port: Int) = getHostAndPort()
                LettuceConnectionFactory(RedisStandaloneConfiguration(host, port), lettucePoolingClientConfiguration())
            }

            Cluster -> {
                logger.info { "post redis cluster mode" }

                LettuceConnectionFactory(RedisClusterConfiguration(nodes), lettucePoolingClientConfiguration())
            }
        }

        return lettuceConnectionFactory.apply {
            // RedisClient.connect 에서 블록킹이 발생하는데, EagerInitialization 를 true로 처리하여 해결할 수 있다.
            eagerInitialization = true
        }
    }

    /*
    * [ Redis Connection Pool을 사용하는 이유 ]
    * - Redis의 multi 명령어를 사용하면 트랜잭션을 사용할 수 있다.
    * - 트랜잭션은 새로운 커넥션을 매 번 생성하고 종료한다. -> 비용 문제
    * - 위와 같은 이유로 커넥션 풀을 사용하려고 한다.
    * - 트랜잭션을 제외한 나머지 명령어의 경우, shareNativeConnection=true(default) 에 의해 하나의 공유된 커넥션을 사용한다.
    * */
    private fun lettucePoolingClientConfiguration(): LettucePoolingClientConfiguration {
        val poolConfig = GenericObjectPoolConfig<Any>()
        poolConfig.maxTotal = maxActive
        poolConfig.maxIdle = maxIdle
        poolConfig.minIdle = minIdle
        poolConfig.setMaxWait(Duration.ofMillis(maxWait))

        return LettucePoolingClientConfiguration.builder()
            .clientName("post-redis-client")
            .readFrom(ReadFrom.REPLICA_PREFERRED)
            .poolConfig(poolConfig)
            .build()
    }

    private fun getHostAndPort(): Pair<String, Int> {
        val splits: List<String> = nodes.first().split(":")
        return Pair(first = splits[0], second = splits[1].toInt())
    }
}

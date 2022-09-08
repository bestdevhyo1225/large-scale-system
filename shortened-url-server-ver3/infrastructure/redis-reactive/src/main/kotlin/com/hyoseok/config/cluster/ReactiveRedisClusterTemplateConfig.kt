package com.hyoseok.config.cluster

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

// @Configuration
class ReactiveRedisClusterTemplateConfig {

//    @Bean
    fun reactiveRedisClusterTemplate1(
        @Qualifier("reactiveRedisClusterConnectionFactory1")
        reactiveRedisClusterConnectionFactory1: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, String?> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisSerializationContext = RedisSerializationContext.newSerializationContext<String, String?>()
            .key(stringRedisSerializer)
            .value(stringRedisSerializer)
            .hashKey(stringRedisSerializer)
            .hashValue(stringRedisSerializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisClusterConnectionFactory1, redisSerializationContext)
    }
}

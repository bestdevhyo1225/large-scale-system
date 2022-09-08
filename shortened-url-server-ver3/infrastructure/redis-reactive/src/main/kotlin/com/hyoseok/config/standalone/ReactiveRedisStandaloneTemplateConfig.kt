package com.hyoseok.config.standalone

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ReactiveRedisStandaloneTemplateConfig {

    @Bean
    fun reactiveRedisTemplate1(
        @Qualifier("reactiveRedisConnectionFactory1")
        reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, String?> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisSerializationContext = RedisSerializationContext.newSerializationContext<String, String?>()
            .key(stringRedisSerializer)
            .value(stringRedisSerializer)
            .hashKey(stringRedisSerializer)
            .hashValue(stringRedisSerializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, redisSerializationContext)
    }

    @Bean
    fun reactiveRedisTemplate2(
        @Qualifier("reactiveRedisConnectionFactory2")
        reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, String?> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisSerializationContext = RedisSerializationContext.newSerializationContext<String, String?>()
            .key(stringRedisSerializer)
            .value(stringRedisSerializer)
            .hashKey(stringRedisSerializer)
            .hashValue(stringRedisSerializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, redisSerializationContext)
    }

    @Bean
    fun reactiveRedisTemplate3(
        @Qualifier("reactiveRedisConnectionFactory3")
        reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisTemplate<String, String?> {
        val stringRedisSerializer = StringRedisSerializer()
        val redisSerializationContext = RedisSerializationContext.newSerializationContext<String, String?>()
            .key(stringRedisSerializer)
            .value(stringRedisSerializer)
            .hashKey(stringRedisSerializer)
            .hashValue(stringRedisSerializer)
            .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, redisSerializationContext)
    }
}

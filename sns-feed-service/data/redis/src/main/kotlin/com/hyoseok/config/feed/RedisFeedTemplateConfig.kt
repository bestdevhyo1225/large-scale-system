package com.hyoseok.config.feed

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisFeedTemplateConfig {

    @Bean
    fun redisFeedTemplate(
        @Qualifier("redisFeedConnectionFactory")
        redisConnectionFactory: RedisConnectionFactory,
    ): RedisTemplate<String, String?> {
        val stringRedisSerializer = StringRedisSerializer()
        return RedisTemplate<String, String?>().apply {
            setConnectionFactory(redisConnectionFactory)
            keySerializer = stringRedisSerializer
            valueSerializer = stringRedisSerializer
            hashKeySerializer = stringRedisSerializer
            hashValueSerializer = stringRedisSerializer
        }
    }
}

package com.hyoseok.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisTemplateConfig {

    @Bean
    fun wishRedisTemplate(
        @Qualifier("wishRedisConnectionFactory")
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

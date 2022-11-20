package com.hyoseok.wish.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.wish.repository.WishRedisRepositoryImpl.ErrorMessage.SADD_RETURN_NULL
import com.hyoseok.wish.repository.WishRedisRepositoryImpl.ErrorMessage.SCARD_RETURN_NULL
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "spring.wish.redis", name = ["enable"], havingValue = "true")
class WishRedisRepositoryImpl(
    @Qualifier("wishRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : WishRedisRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    object ErrorMessage {
        const val SADD_RETURN_NULL = "sadd 명령 수행 후, NULL 값이 반환됨"
        const val SCARD_RETURN_NULL = "scard 명령 수행 후, NULL 값이 반환됨"
    }

    override fun <T : Any> sadd(key: String, value: T): Long =
        redisTemplate.opsForSet().add(key, jacksonObjectMapper.writeValueAsString(value))
            ?: throw RuntimeException(SADD_RETURN_NULL)

    override fun scard(key: String): Long =
        redisTemplate.opsForSet().size(key) ?: throw RuntimeException(SCARD_RETURN_NULL)
}

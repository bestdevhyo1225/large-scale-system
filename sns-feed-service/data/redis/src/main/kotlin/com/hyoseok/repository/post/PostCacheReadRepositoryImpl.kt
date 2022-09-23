package com.hyoseok.repository.post

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.post.repository.PostCacheReadRepository
import com.hyoseok.repository.AbstractCacheRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
@ConditionalOnProperty(prefix = "data.enable.redis", name = ["post"], havingValue = "true")
class PostCacheReadRepositoryImpl(
    @Qualifier("redisPostTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : PostCacheReadRepository, AbstractCacheRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T> get(key: String, clazz: Class<T>): T? {
        val remainingExpiryTimeMS: Long = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS)

        if (isRefreshKey(remainingExpiryTimeMS = remainingExpiryTimeMS)) {
            return null
        }

        val value: String? = redisTemplate.opsForValue().get(key)

        if (value.isNullOrBlank()) {
            return null
        }

        return jacksonObjectMapper.readValue(value, clazz)
    }

    override fun <T> mget(keys: List<String>, clazz: Class<T>): List<T> {
        val values: List<String?>? = redisTemplate.opsForValue().multiGet(keys)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.fold(mutableListOf()) { acc, value ->
            if (!value.isNullOrBlank()) {
                acc.add(jacksonObjectMapper.readValue(value, clazz))
            }
            acc
        }
    }

    override fun <T> zrevrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun zcard(key: String): Long = redisTemplate.opsForZSet().zCard(key) ?: 0L
}

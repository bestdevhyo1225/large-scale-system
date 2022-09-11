package com.hyoseok.repository.sns.read

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.repository.AbstractCacheRepository
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.read.SnsCacheReadRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class SnsCacheReadRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String?>,
) : SnsCacheReadRepository, AbstractCacheRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun get(key: String, clazz: Class<SnsCache>): SnsCache? {
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

    override fun mget(keys: List<String>, clazz: Class<SnsCache>): List<SnsCache> {
        val values: List<String?>? = redisTemplate.opsForValue().multiGet(keys)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        if (values.any { it.isNullOrBlank() }) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }

    override fun zrevrangeString(key: String, startIndex: Long, endIndex: Long): List<String> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, startIndex, endIndex)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { it.toString() }
    }

    override fun zcard(key: String): Long = redisTemplate.opsForZSet().zCard(key) ?: 0L
}

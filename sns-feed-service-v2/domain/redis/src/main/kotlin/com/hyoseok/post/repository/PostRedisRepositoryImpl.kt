package com.hyoseok.post.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.common.AbstractRedisRepository
import com.hyoseok.post.entity.PostCache
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

@Repository
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisRepositoryImpl(
    @Qualifier("postRedisTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : PostRedisRepository, AbstractRedisRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T> get(key: String, clazz: Class<T>): T? {
        val remainingExpiryTimeMS: Long = redisTemplate.getExpire(key, MILLISECONDS)

        if (isRefreshKey(remainingExpiryTimeMS = remainingExpiryTimeMS)) {
            return null
        }

        val value: String? = redisTemplate.opsForValue().get(key)

        if (value.isNullOrBlank()) {
            return null
        }

        return jacksonObjectMapper.readValue(value, clazz)
    }

    override fun <HK, HV : Any> hget(key: String, hashKey: HK, clazz: Class<HV>): HV? {
        val value: String? =
            redisTemplate.opsForHash<String, String>().get(key, jacksonObjectMapper.writeValueAsString(hashKey))

        if (value.isNullOrBlank()) {
            return null
        }

        return jacksonObjectMapper.readValue(value, clazz)
    }

    override fun <HK, HV : Any> hset(key: String, hashKey: HK, value: HV) {
        redisTemplate.opsForHash<String, String>()
            .put(key, jacksonObjectMapper.writeValueAsString(hashKey), jacksonObjectMapper.writeValueAsString(value))
    }

    override fun <HK : Any> hIncrement(key: String, hashKey: HK, value: Long): Long =
        redisTemplate.opsForHash<String, String>()
            .increment(key, jacksonObjectMapper.writeValueAsString(hashKey), value)

    override fun increment(key: String): Long = redisTemplate.opsForValue().increment(key) ?: 0

    override fun mget(keys: List<String>): List<PostCache> {
        val values: List<String?>? = redisTemplate.opsForValue().multiGet(keys)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.fold(mutableListOf()) { acc, value ->
            if (!value.isNullOrBlank()) {
                acc.add(jacksonObjectMapper.readValue(value, PostCache::class.java))
            }
            acc
        }
    }

    override fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue()
            .set(key, jacksonObjectMapper.writeValueAsString(value), expireTime, timeUnit)
    }

    override fun setAllUsePipeline(keysAndValues: Map<String, PostCache>, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.executePipelined {
            keysAndValues.forEach { (key: String, value: PostCache) ->
                set(key = key, value = value, expireTime = expireTime, timeUnit = timeUnit)
            }
            return@executePipelined null
        }
    }

    override fun <T : Any> zadd(key: String, value: T, score: Double) {
        redisTemplate.opsForZSet().add(key, jacksonObjectMapper.writeValueAsString(value), score)
    }

    override fun zcard(key: String): Long = redisTemplate.opsForZSet().zCard(key) ?: 0L

    override fun zremRangeByRank(key: String, start: Long, end: Long) {
        redisTemplate.opsForZSet().removeRange(key, start, end)
    }

    override fun <T : Any> zrevRange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }
}

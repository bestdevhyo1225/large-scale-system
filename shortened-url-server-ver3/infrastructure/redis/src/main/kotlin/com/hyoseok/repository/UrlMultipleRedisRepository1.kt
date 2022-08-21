package com.hyoseok.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class UrlMultipleRedisRepository1(
    @Qualifier("redisMultipleTemplate1")
    private val redisTemplate: RedisTemplate<String, String?>,
) : UrlCacheRepository, AbstractRedisRepository() {

    override fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue()
            .set(key, jacksonObjectMapper().writeValueAsString(value), expireTime, timeUnit)
    }

    override fun <T : Any> get(key: String, clazz: Class<T>): T? {
        if (shouldRefreshKey(key = key)) {
            return null
        }

        val value = redisTemplate.opsForValue()
            .get(key)

        if (value.isNullOrBlank()) {
            return null
        }

        return jacksonObjectMapper().readValue(value, clazz)
    }


    private fun shouldRefreshKey(key: String, expireTimeGapMs: Long = 3_000L): Boolean {
        val remainingExpiryTimeMS = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS)
        return remainingExpiryTimeMS >= 0 &&
            getExpiryTimeBasedOnPER(remainingExpiryTimeMS = remainingExpiryTimeMS, delta = expireTimeGapMs) <= 0.0f
    }
}

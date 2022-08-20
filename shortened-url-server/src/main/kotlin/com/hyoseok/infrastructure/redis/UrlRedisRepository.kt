package com.hyoseok.infrastructure.redis

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.domain.repository.UrlCacheRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.ln

@Repository
class UrlRedisRepository(
    private val redisTemplate: RedisTemplate<String, String?>,
) : UrlCacheRepository {

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
        return remainingExpiryTimeMS >= 0
            && getExpiryTimeBasedOnPER(remainingExpiryTimeMS = remainingExpiryTimeMS, delta = expireTimeGapMs) <= 0.0f
    }

    /**
     * [ remainingExpiryTimeMS ]
     * - 남은 만료 시간
     * [ delta ]
     * - 캐시를 다시 계산하기 위한 시간 범위 (단위: MS)
     * [ beta ]
     * - 가중치 (기본 값으로 1.0을 사용한다.)
     * - ex) beta < 1.0 => 조금 더 소극적으로 재 계산한다.
     * - ex) beta > 1.0 => 조금 더 적극적으로 재 계산한다.
     * */
    protected fun getExpiryTimeBasedOnPER(remainingExpiryTimeMS: Long, delta: Long, beta: Float = 1.0f): Double {
        return remainingExpiryTimeMS - abs(delta * beta * ln(Math.random()))
    }
}

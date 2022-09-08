package com.hyoseok.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class UrlReactiveRedisStandaloneRepository2(
    @Qualifier("reactiveRedisTemplate2")
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String?>,
) : UrlCacheNonBlockingRepository, AbstractReactiveRedisRepository() {

    override suspend fun <T : Any> set(key: String, value: T, expireDuration: Duration) {
        reactiveRedisTemplate.opsForValue()
            .set(key, jacksonObjectMapper().writeValueAsString(value), expireDuration)
            .awaitSingle() // Mono<Boolean!> -> Boolean!
    }

    override suspend fun <T : Any> get(key: String, clazz: Class<T>): T? {
        if (shouldRefreshKey(key = key)) {
            return null
        }

        val value: String? = reactiveRedisTemplate.opsForValue()
            .get(key)
            .awaitSingleOrNull() // Mono<String?> -> String?

        if (value.isNullOrBlank()) {
            return null
        }

        return jacksonObjectMapper().readValue(value, clazz)
    }

    private suspend fun shouldRefreshKey(key: String, expireTimeGapMs: Long = 3_000L): Boolean {
        val remainingExpiryTimeMS: Long = reactiveRedisTemplate
            .getExpire(key)
            .awaitSingleOrNull() // Mono<Duration?> -> Duration?
            ?.toMillis() ?: -1L

        return remainingExpiryTimeMS >= 0 &&
            getExpiryTimeBasedOnPER(remainingExpiryTimeMS = remainingExpiryTimeMS, delta = expireTimeGapMs) <= 0.0f
    }
}

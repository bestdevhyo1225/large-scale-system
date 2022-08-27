package com.hyoseok.repository

import com.hyoseok.config.standalone.multiple.property.RedisServers
import com.hyoseok.constants.CircuitBreakerName
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class UrlRedisRepository(
    private val redisServers: RedisServers,
    @Qualifier("urlMultipleRedisRepository1")
    private val urlCacheRepository1: UrlCacheRepository,
    @Qualifier("urlMultipleRedisRepository2")
    private val urlCacheRepository2: UrlCacheRepository,
    @Qualifier("urlMultipleRedisRepository3")
    private val urlCacheRepository3: UrlCacheRepository,
) : UrlCacheRepository {

    private val logger = KotlinLogging.logger {}
    private val minRedisServerCount = 1

    override fun <T : Any> set(key: String, value: T, expireTime: Long, timeUnit: TimeUnit) {
        CoroutineScope(context = Dispatchers.IO).launch {
            urlCacheRepository1.set(key = key, value = value, expireTime = expireTime, timeUnit = timeUnit)
            urlCacheRepository2.set(key = key, value = value, expireTime = expireTime, timeUnit = timeUnit)
            urlCacheRepository3.set(key = key, value = value, expireTime = expireTime, timeUnit = timeUnit)
        }
    }

    @CircuitBreaker(name = CircuitBreakerName.NOSQL_REDIS, fallbackMethod = "getNull")
    override fun <T : Any> get(key: String, clazz: Class<T>): T? =
        when (getNodeIndex()) {
            0 -> urlCacheRepository1.get(key = key, clazz = clazz)
            1 -> urlCacheRepository2.get(key = key, clazz = clazz)
            2 -> urlCacheRepository3.get(key = key, clazz = clazz)
            else -> urlCacheRepository1.get(key = key, clazz = clazz)
        }

    private fun <T : Any> getNull(exception: Exception): T? {
        logger.info { "fallback: ${exception.localizedMessage}" }
        return null
    }

    private fun getNodeIndex(): Int = ((minRedisServerCount..redisServers.nodes.size).random()).minus(1)
}

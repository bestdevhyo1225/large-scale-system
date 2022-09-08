package com.hyoseok.repository.standalone

import com.hyoseok.config.RedisServerCounts
import com.hyoseok.config.property.RedisServers
import com.hyoseok.repository.UrlCacheNonBlockingRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class UrlReactiveRedisStandaloneRepository(
    private val redisServers: RedisServers,
    @Qualifier("urlReactiveRedisStandaloneRepository1")
    private val urlReactiveRedisStandaloneRepository1: UrlCacheNonBlockingRepository,
    @Qualifier("urlReactiveRedisStandaloneRepository2")
    private val urlReactiveRedisStandaloneRepository2: UrlCacheNonBlockingRepository,
    @Qualifier("urlReactiveRedisStandaloneRepository3")
    private val urlReactiveRedisStandaloneRepository3: UrlCacheNonBlockingRepository,
) : UrlCacheNonBlockingRepository {

    override suspend fun <T : Any> set(key: String, value: T, expireDuration: Duration) {
        urlReactiveRedisStandaloneRepository1.set(key = key, value = value, expireDuration = expireDuration)
        urlReactiveRedisStandaloneRepository2.set(key = key, value = value, expireDuration = expireDuration)
        urlReactiveRedisStandaloneRepository3.set(key = key, value = value, expireDuration = expireDuration)
    }

    override suspend fun <T : Any> get(key: String, clazz: Class<T>): T? =
        when (getNodeIndex()) {
            0 -> urlReactiveRedisStandaloneRepository1.get(key = key, clazz = clazz)
            1 -> urlReactiveRedisStandaloneRepository2.get(key = key, clazz = clazz)
            2 -> urlReactiveRedisStandaloneRepository3.get(key = key, clazz = clazz)
            else -> urlReactiveRedisStandaloneRepository1.get(key = key, clazz = clazz)
        }

    private fun getNodeIndex(): Int =
        ((RedisServerCounts.URL_MIN_SERVER_COUNT..redisServers.nodes.size).random()).minus(1)
}

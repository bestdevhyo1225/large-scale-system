package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes
import com.hyoseok.config.RedisKeys
import com.hyoseok.repository.UrlCacheNonBlockingRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class UrlFacadeService(
    private val urlService: UrlService,
    @Qualifier("urlReactiveRedisRepository")
    private val urlReactiveRedisRepository: UrlCacheNonBlockingRepository,
) {

    private val logger = KotlinLogging.logger {}

    suspend fun find(encodedUrl: String): String {
        val key: String = RedisKeys.getUrlKey(encodedUrl = encodedUrl)

        urlReactiveRedisRepository.get(key = key, clazz = String::class.java)
            ?.let { return it }

        logger.info { "cache miss!" }

        val longUrl: String = urlService.findLongUrl(encodedUrl = encodedUrl)

        urlReactiveRedisRepository.set(
            key = key,
            value = longUrl,
            expireDuration = Duration.ofSeconds(RedisExpireTimes.URLS),
        )

        return longUrl
    }
}

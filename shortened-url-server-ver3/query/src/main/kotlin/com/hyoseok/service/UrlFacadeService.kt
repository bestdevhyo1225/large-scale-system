package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes
import com.hyoseok.config.RedisKeys
import com.hyoseok.repository.UrlCacheRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UrlFacadeService(
    @Qualifier("urlRedisRepository")
    private val urlCacheRepository: UrlCacheRepository,
    private val urlService: UrlService,
) {

    private val logger = KotlinLogging.logger {}

    fun find(encodedUrl: String): String {
        val key: String = RedisKeys.getUrlKey(encodedUrl = encodedUrl)

        urlCacheRepository.get(key = key, clazz = String::class.java)
            ?.let { return it }

        logger.info { "cache miss!" }

        val longUrl: String = urlService.findLongUrl(encodedUrl = encodedUrl)

        urlCacheRepository.set(
            key = key,
            value = longUrl,
            expireTime = RedisExpireTimes.URLS,
            timeUnit = TimeUnit.SECONDS,
        )

        return longUrl
    }
}

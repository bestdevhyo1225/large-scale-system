package com.hyoseok.service

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UrlFacadeService(
    private val urlService: UrlService,
) {

    private val logger = KotlinLogging.logger {}

    suspend fun find(encodedUrl: String): String {
//        val key: String = RedisKeys.getUrlKey(encodedUrl = encodedUrl)
//
//        urlCacheRepository.get(key = key, clazz = String::class.java)
//            ?.let { return it }

        logger.info { "cache miss!" }

        val longUrl: String = urlService.findLongUrl(encodedUrl = encodedUrl)

//        urlCacheRepository.set(
//            key = key,
//            value = longUrl,
//            expireTime = RedisExpireTimes.URLS,
//            timeUnit = TimeUnit.SECONDS,
//        )

        return longUrl
    }
}

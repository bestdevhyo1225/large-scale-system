package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes
import com.hyoseok.config.RedisKeys
import com.hyoseok.repository.UrlCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UrlFacadeService(
    @Qualifier("urlRedisRepository")
    private val urlCacheRepository: UrlCacheRepository,
) {

    private val logger = KotlinLogging.logger {}

    fun find(shortUrl: String): String {
        val key: String = RedisKeys.getUrlsKey(shortUrl = shortUrl)

        urlCacheRepository.get(key = key, clazz = String::class.java)
            ?.let { return it }

        logger.info { "cache miss!" }

//        val longUrl: String = urlService.find(shortUrl = shortUrl)
        val longUrl = "https://hyos-dev-log.tistory.com/"

        urlCacheRepository.set(
            key = key,
            value = longUrl,
            expireTime = RedisExpireTimes.URLS,
            timeUnit = TimeUnit.SECONDS,
        )

        return longUrl
    }
}

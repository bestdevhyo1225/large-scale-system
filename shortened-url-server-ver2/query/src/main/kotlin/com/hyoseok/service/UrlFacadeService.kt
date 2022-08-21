package com.hyoseok.service

import com.hyoseok.config.redis.RedisExpireTimes
import com.hyoseok.config.redis.RedisKeys
import com.hyoseok.repository.UrlCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class UrlFacadeService(
    private val urlCacheRepository: UrlCacheRepository,
    private val urlService: UrlService,
) {

    private val logger = KotlinLogging.logger {}

    fun find(shortUrl: String): String {
        val key: String = RedisKeys.getUrlsKey(shortUrl = shortUrl)

        urlCacheRepository.get(key = key, clazz = String::class.java)
            ?.let { return it }

        logger.info { "cache miss!" }

        val longUrl: String = urlService.find(shortUrl = shortUrl)

        CoroutineScope(context = Dispatchers.IO).launch {
            urlCacheRepository.set(
                key = key,
                value = longUrl,
                expireTime = RedisExpireTimes.URLS,
                timeUnit = TimeUnit.SECONDS,
            )
        }

        return longUrl
    }
}

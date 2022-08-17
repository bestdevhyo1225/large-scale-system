package com.hyoseok.service

import com.hyoseok.common.constants.ServerExceptionMessage
import com.hyoseok.domain.entity.Url
import com.hyoseok.domain.repository.UrlRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UrlService(
    private val urlRepository: UrlRepository,
) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    fun create(longUrl: String): String {
        urlRepository.findByLongUrl(longUrl = longUrl)
            ?.let {
                logger.info { "is Exist Url: $it" }
                return it.shortUrl
            }

        val savedUrl = urlRepository.save(Url(longUrl = longUrl))

        logger.info { "saved Url: $savedUrl" }

        return savedUrl.shortUrl
    }

    fun find(shortUrl: String): String {
        logger.info { "current shortUrl: $shortUrl" }

        val url: Url = urlRepository.findByShortUrl(shortUrl = shortUrl)
            ?: throw NoSuchElementException(ServerExceptionMessage.URL_NOT_FOUND)

        logger.info { "find Url: $url" }

        return url.longUrl
    }
}

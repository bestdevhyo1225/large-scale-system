package com.hyoseok.service

import com.hyoseok.constants.ServerExceptionMessage
import com.hyoseok.entity.Url
import com.hyoseok.repository.UrlRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UrlService(
    private val urlRepository: UrlRepository,
) {

    private val logger = KotlinLogging.logger {}

    fun find(shortUrl: String): String {
        logger.info { "current shortUrl: $shortUrl" }

        val url: Url = urlRepository.findByShortUrl(shortUrl = shortUrl)
            ?: throw NoSuchElementException(ServerExceptionMessage.URL_NOT_FOUND)

        logger.info { "longUrl: ${url.longUrl}" }

        return url.longUrl
    }
}

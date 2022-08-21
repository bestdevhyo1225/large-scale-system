package com.hyoseok.service

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
}

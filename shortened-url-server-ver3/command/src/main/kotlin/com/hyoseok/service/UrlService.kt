package com.hyoseok.service

import com.hyoseok.entity.Url
import com.hyoseok.repository.UrlRepository
import com.hyoseok.utils.Sha256Util
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UrlService(
    @Qualifier("urlJpaRepositoryAdapter")
    private val urlRepository: UrlRepository,
) {

    private val logger = KotlinLogging.logger {}

    @Transactional
    fun create(longUrl: String): String {
        val encodedUrl = Sha256Util.encode(value = longUrl)
        urlRepository.findByEncodedUrl(encodedUrl = encodedUrl)?.let { return it.encodedUrl }

        try {
            urlRepository.save(url = Url(encodedUrl = encodedUrl, longUrl = longUrl))
        } catch (exception: DataIntegrityViolationException) {
            logger.info { "Already registered url" }
        }

        return encodedUrl
    }

    @Transactional
    fun findByEncodedUrl(longUrl: String): String {
        val url: Url = urlRepository.findByLongUrl(longUrl = longUrl) ?: throw NoSuchElementException("")
        return url.encodedUrl
    }
}

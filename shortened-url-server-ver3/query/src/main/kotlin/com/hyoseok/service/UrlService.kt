package com.hyoseok.service

import com.hyoseok.repository.UrlRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UrlService(
    @Qualifier("urlJpaRepositoryAdapter")
    private val urlRepository: UrlRepository,
) {

    fun findLongUrl(shortUrl: String): String =
        (urlRepository.findByShortUrl(shortUrl = shortUrl) ?: throw NoSuchElementException("Not Found!")).longUrl
}

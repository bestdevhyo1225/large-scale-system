package com.hyoseok.service

import com.hyoseok.repository.UrlNonBlockingRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class UrlService(
    @Qualifier("urlNonBlockingRepositoryAdapter")
    private val urlRepository: UrlNonBlockingRepository,
) {

    suspend fun findLongUrl(encodedUrl: String) =
        (urlRepository.findByEncodedUrl(encodedUrl = encodedUrl) ?: throw NoSuchElementException("Not Found!")).longUrl
}

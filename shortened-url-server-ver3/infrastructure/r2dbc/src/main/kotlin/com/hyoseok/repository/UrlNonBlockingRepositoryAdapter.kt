package com.hyoseok.repository

import com.hyoseok.entity.Url
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class UrlNonBlockingRepositoryAdapter(
    private val urlRepository: UrlCoroutineRepository,
) : UrlNonBlockingRepository {

    override suspend fun findByEncodedUrl(encodedUrl: String): Url? =
        urlRepository.findByEncodedUrl(encodedUrl = encodedUrl)?.toDomainEntity()
}

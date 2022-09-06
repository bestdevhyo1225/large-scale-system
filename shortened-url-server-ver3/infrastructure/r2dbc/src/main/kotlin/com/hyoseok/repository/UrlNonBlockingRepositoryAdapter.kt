package com.hyoseok.repository

import com.hyoseok.entity.Url
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class UrlNonBlockingRepositoryAdapter(
    private val urlCoroutineRepository: UrlCoroutineRepository,
) : UrlNonBlockingRepository {

    override suspend fun findByEncodedUrl(encodedUrl: String): Url? =
        urlCoroutineRepository.findByEncodedUrl(encodedUrl = encodedUrl)?.toDomainEntity()
}

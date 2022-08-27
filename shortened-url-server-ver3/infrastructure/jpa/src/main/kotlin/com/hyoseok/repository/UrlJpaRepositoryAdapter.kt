package com.hyoseok.repository

import com.hyoseok.constants.CircuitBreakerName
import com.hyoseok.entity.Url
import com.hyoseok.entity.UrlJpaEntity
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import mu.KotlinLogging
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
@Transactional(readOnly = true)
class UrlJpaRepositoryAdapter(
    private val urlJpaRepository: UrlJpaRepository,
) : UrlRepository {

    private val logger = KotlinLogging.logger {}

    override fun save(url: Url) {
        urlJpaRepository.save(UrlJpaEntity(url = url))
    }

    override fun findByLongUrl(longUrl: String): Url? =
        urlJpaRepository.findByLongUrl(longUrl = longUrl)?.toDomainEntity()

    override fun findByShortUrl(shortUrl: String): Url? =
        urlJpaRepository.findByShortUrl(shortUrl = shortUrl)?.toDomainEntity()

    private fun findTempUrl(exception: Exception): Url {
        logger.info { "fallback: ${exception.localizedMessage}" }
        return Url(id = 1, shortUrl = "shortUrl", longUrl = "longUrl", createdAt = LocalDateTime.now())
    }
}

package com.hyoseok.service

import com.hyoseok.config.RedissonUseType
import com.hyoseok.entity.Url
import com.hyoseok.repository.UrlRepository
import com.hyoseok.utils.Sha256Util
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UrlService(
    @Qualifier("urlJpaRepositoryAdapter")
    private val urlRepository: UrlRepository,
    private val distributedLockService: RedissonDistributedLockService,
) {

    @Transactional
    fun create(longUrl: String): String {
        val shortUrl = Sha256Util.encode(value = longUrl)
        return distributedLockService.executeWithLock(value = shortUrl, useType = RedissonUseType.URL) {
            urlRepository.findByShortUrl(shortUrl = shortUrl)?.let { return@executeWithLock it.shortUrl }
            urlRepository.save(url = Url(shortUrl = shortUrl, longUrl = longUrl))
            shortUrl
        }
    }
}

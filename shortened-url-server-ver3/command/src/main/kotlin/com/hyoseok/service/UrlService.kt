package com.hyoseok.service

import com.hyoseok.config.RedissonUseType
import com.hyoseok.entity.Url
import com.hyoseok.repository.UrlRepository
import com.hyoseok.utils.Base62Util
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
        val shortUrl = Base62Util.encode(value = System.currentTimeMillis())

        return distributedLockService.executeWithLock(value = shortUrl, useType = RedissonUseType.SHORT_URL) {
            urlRepository.findByLongUrl(longUrl = longUrl)?.let { return@executeWithLock it.shortUrl }

            val url = Url(shortUrl = shortUrl, longUrl = longUrl)

            urlRepository.save(url)

            url.shortUrl
        }
    }
}

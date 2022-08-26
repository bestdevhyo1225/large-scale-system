package com.hyoseok.service

import com.hyoseok.entity.Url
import com.hyoseok.repository.UrlRepository
import com.hyoseok.utils.Base62Util
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
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
        urlRepository.findByLongUrl(longUrl = longUrl)
            ?.let {
                logger.info { "is Exist Url: $it" }
                return it.shortUrl
            }

        val id = System.currentTimeMillis()
        val shortUrl = Base62Util.encode(value = id)
        val url = Url(id = id, shortUrl = shortUrl, longUrl = longUrl)

        urlRepository.save(url)

        logger.info { "saved Url: $url" }

        return url.shortUrl
    }
}

package com.hyoseok.service

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
) {

    @Transactional
    fun create(longUrl: String): String {
        urlRepository.findByLongUrl(longUrl = longUrl)?.let { return it.shortUrl }

        val url = Url(shortUrl = Base62Util.encode(value = System.currentTimeMillis()), longUrl = longUrl)

        urlRepository.save(url)

        return url.shortUrl
    }
}

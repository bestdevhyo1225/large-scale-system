package com.hyoseok.repository

import com.hyoseok.entity.Url

interface UrlRepository {
    fun save(url: Url)
    fun findByLongUrl(longUrl: String): Url?
    fun findByEncodedUrl(encodedUrl: String): Url?
}

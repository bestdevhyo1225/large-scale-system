package com.hyoseok.repository

import com.hyoseok.entity.Url

interface UrlNonBlockingRepository {
    suspend fun findByEncodedUrl(encodedUrl: String): Url?
}

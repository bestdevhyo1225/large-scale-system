package com.hyoseok.domain.repository

import com.hyoseok.domain.entity.Url
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UrlRepository : JpaRepository<Url, Long> {

    @Query("SELECT u FROM Url u WHERE u.longUrl = :longUrl")
    fun findByLongUrl(longUrl: String): Url?

    @Query("SELECT u FROM Url u WHERE u.shortUrl = :shortUrl")
    fun findByShortUrl(shortUrl: String): Url?
}

package com.hyoseok.repository

import com.hyoseok.entity.UrlJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UrlJpaRepository : JpaRepository<UrlJpaEntity, Long> {

    @Query("SELECT u FROM UrlJpaEntity u WHERE u.longUrl = :longUrl")
    fun findByLongUrl(longUrl: String): UrlJpaEntity?

    @Query("SELECT u FROM UrlJpaEntity u WHERE u.shortUrl = :shortUrl")
    fun findByShortUrl(shortUrl: String): UrlJpaEntity?
}

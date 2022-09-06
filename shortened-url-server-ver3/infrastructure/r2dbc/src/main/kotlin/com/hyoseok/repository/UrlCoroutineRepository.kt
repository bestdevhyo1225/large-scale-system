package com.hyoseok.repository

import com.hyoseok.entity.UrlEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlCoroutineRepository : CoroutineCrudRepository<UrlEntity, Long> {

    suspend fun findUrlEntityByEncodedUrl(encodedUrl: String): UrlEntity?
}

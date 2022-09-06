package com.hyoseok.repository

import com.hyoseok.entity.UrlEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UrlReactiveRepository : ReactiveCrudRepository<UrlEntity, Long> {

    fun findByEncodedUrl(encodedUrl: String): Mono<UrlEntity>
}

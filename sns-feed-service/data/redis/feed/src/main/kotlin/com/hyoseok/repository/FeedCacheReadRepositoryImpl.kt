package com.hyoseok.repository

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.feed.repository.FeedCacheReadRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class FeedCacheReadRepositoryImpl(
    @Qualifier("redisFeedTemplate")
    private val redisTemplate: RedisTemplate<String, String?>,
) : FeedCacheReadRepository, AbstractCacheRepository() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T> zrevrange(key: String, start: Long, end: Long, clazz: Class<T>): List<T> {
        val values: Set<String?>? = redisTemplate.opsForZSet().reverseRange(key, start, end)

        if (values.isNullOrEmpty()) {
            return listOf()
        }

        return values.map { jacksonObjectMapper.readValue(it, clazz) }
    }
}

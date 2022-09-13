package com.hyoseok.repository.sns

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.SnsCacheRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class SnsCacheRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String?>,
) : SnsCacheRepository {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun setex(key: String, value: SnsCache, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.opsForValue()
            .set(key, jacksonObjectMapper.writeValueAsString(value), expireTime, timeUnit)
    }

    override fun setAllEx(keysAndValues: List<Pair<String, SnsCache>>, expireTime: Long, timeUnit: TimeUnit) {
        redisTemplate.executePipelined {
            keysAndValues.forEach { (key, value) ->
                setex(key = key, value = value, expireTime = expireTime, timeUnit = timeUnit)
            }
            return@executePipelined null
        }
    }

    override fun zaddString(key: String, value: String, score: Double) {
        redisTemplate.opsForZSet().add(key, value, score)
    }

    override fun zremString(key: String, value: String) {
        redisTemplate.opsForZSet().remove(key, value)
    }

    override fun zremStringRangeByRank(key: String, startIndex: Long, endIndex: Long) {
        redisTemplate.opsForZSet().removeRange(key, startIndex, endIndex)
    }
}

package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes.SNS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.SNS_ZSET_KEY
import com.hyoseok.config.RedisZsetScores
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.SnsCacheRepository
import com.hyoseok.sns.repository.read.SnsCacheReadRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
class SnsFacadeService(
    private val snsCacheReadRepository: SnsCacheReadRepository,
    private val snsCacheRepository: SnsCacheRepository,
    private val snsQueryService: SnsQueryService,
) {

    fun findWithAssociatedEntitiesById(snsId: Long): SnsFindResultDto {
        val key = RedisKeys.getSnsKey(id = snsId)

        snsCacheReadRepository.get(key = key, clazz = SnsCache::class.java)
            ?.let { return SnsFindResultDto(snsCache = it) }

        val snsCache = snsQueryService.findWithAssociatedEntitiesById(snsId = snsId)
            .toCacheDto()
        val score = RedisZsetScores.getTimestampCreatedAt(createdAt = snsCache.createdAt)

        CoroutineScope(context = Dispatchers.IO).launch {
            snsCacheRepository.zaddString(key = SNS_ZSET_KEY, value = key, score = score)
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
        }

        return SnsFindResultDto(snsCache = snsCache)
    }

    fun findAllByLimitAndOffset(start: Long, count: Long): Pair<List<SnsFindResultDto>, Long> {
        // DB 등록시, 캐시도 등록되어야 작성된 코드가 의미가 있음
        val snsKeys: List<String> = snsCacheReadRepository.zrevrangeString(
            key = SNS_ZSET_KEY,
            startIndex = start,
            endIndex = start.plus(count).minus(1),
        )
        val snsKeyTotalCount: Long = snsCacheReadRepository.zcardSnsKeys(key = SNS_ZSET_KEY)
        val snsCaches: List<SnsCache> = snsCacheReadRepository.mget(keys = snsKeys, clazz = SnsCache::class.java)

        // 조건에 만족하면, 만료된 캐시가 없다는 의미
        if (snsCaches.size == snsKeys.size) {
            return Pair(first = snsCaches.map { SnsFindResultDto(snsCache = it) }, second = snsKeyTotalCount)
        }

        val (snsList, totalCount) = snsQueryService.findAllByLimitAndOffset(limit = count, offset = start)

        CoroutineScope(context = Dispatchers.IO).launch {
            snsCacheRepository.setAllEx(
                keysAndValues = snsList.map {
                    Pair(first = RedisKeys.getSnsKey(id = it.id!!), second = it.toCacheDto())
                },
                expireTime = SNS,
                timeUnit = SECONDS,
            )
        }

        return Pair(first = snsList.map { SnsFindResultDto(snsCache = it.toCacheDto()) }, second = totalCount)
    }
}
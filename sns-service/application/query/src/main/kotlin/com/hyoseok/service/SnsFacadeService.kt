package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes.SNS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.SNS_ZSET_KEY
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.SnsCacheRepository
import com.hyoseok.sns.repository.read.SnsCacheReadRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.concurrent.TimeUnit.SECONDS

@Service
class SnsFacadeService(
    private val snsCacheReadRepository: SnsCacheReadRepository,
    private val snsCacheRepository: SnsCacheRepository,
    private val snsService: SnsService,
) {

    fun findWithAssociatedEntitiesById(snsId: Long): SnsFindResultDto {
        val key = RedisKeys.getSnsKey(id = snsId)

        snsCacheReadRepository.get(key = key, clazz = SnsCache::class.java)
            ?.let { return SnsFindResultDto(snsCache = it) }

        val snsCache: SnsCache = snsService.findWithAssociatedEntitiesById(snsId = snsId)
            .toCacheDto()

        CoroutineScope(context = Dispatchers.IO).launch {
            // 트랜잭션 처리가 필요한가?
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
            snsCacheRepository.zaddSnsKeys(
                key = SNS_ZSET_KEY,
                value = key,
                score = Timestamp.valueOf(snsCache.createdAt).time.toDouble(),
            )
        }

        return SnsFindResultDto(snsCache = snsCache)
    }

    fun findAllByLimitAndOffset(start: Long, count: Long): Pair<List<SnsFindResultDto>, Long> {
        val (snsList, totalCount) = snsService.findAllByLimitAndOffset(limit = count, offset = start)
        val snsCaches = snsList.map { SnsFindResultDto(snsCache = it.toCacheDto()) }
        return Pair(first = snsCaches, second = totalCount)
    }
}

package com.hyoseok.service

import com.hyoseok.config.RedisCommons.ZSET_MAX_LIMIT
import com.hyoseok.config.RedisExpireTimes.SNS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.SNS_ZSET_KEY
import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.service.dto.SnsEditDto
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.SnsCacheRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.concurrent.TimeUnit.SECONDS

@Service
class SnsFacadeService(
    private val snsCommandService: SnsCommandService,
    private val snsCacheRepository: SnsCacheRepository,
) {

    fun create(dto: SnsCreateDto): SnsCreateResultDto {
        val sns: Sns = snsCommandService.create(dto = dto)
        val key: String = RedisKeys.getSnsKey(id = sns.id!!)
        val snsCache: SnsCache = sns.toCacheDto()
        val score: Double = Timestamp.valueOf(snsCache.createdAt).time.toDouble()

        CoroutineScope(context = Dispatchers.IO).launch {
            // 트랜잭션 시작
            snsCacheRepository.zaddString(key = SNS_ZSET_KEY, value = key, score = score)
            snsCacheRepository.zremStringRangeByRank(key = SNS_ZSET_KEY, start = ZSET_MAX_LIMIT, end = ZSET_MAX_LIMIT)
            // 트랜잭션 종료
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
        }

        return SnsCreateResultDto(snsId = sns.id!!)
    }

    fun edit(dto: SnsEditDto) {
        val sns: Sns = snsCommandService.edit(dto = dto)
        val key: String = RedisKeys.getSnsKey(id = sns.id!!)
        val snsCache: SnsCache = sns.toCacheDto()

        CoroutineScope(context = Dispatchers.IO).launch {
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
        }
    }

    fun delete(id: Long) {
        snsCommandService.delete(id = id)

        val key: String = RedisKeys.getSnsKey(id = id)

        CoroutineScope(context = Dispatchers.IO).launch {
            snsCacheRepository.zremString(key = SNS_ZSET_KEY, value = key)
            snsCacheRepository.del(key = key)
        }
    }
}

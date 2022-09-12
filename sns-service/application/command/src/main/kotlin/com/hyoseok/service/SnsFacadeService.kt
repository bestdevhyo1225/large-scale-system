package com.hyoseok.service

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

        CoroutineScope(context = Dispatchers.IO).launch {
            val key: String = RedisKeys.getSnsHashKey(id = sns.id!!)
            val snsCache: SnsCache = sns.toCacheDto()
            val score: Double = Timestamp.valueOf(snsCache.createdAt).time.toDouble()

            snsCacheRepository.zaddString(key = SNS_ZSET_KEY, value = key, score = score)
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
        }

        return SnsCreateResultDto(snsId = sns.id!!)
    }

    fun edit(dto: SnsEditDto) {
        val sns: Sns = snsCommandService.edit(dto = dto)

        CoroutineScope(context = Dispatchers.IO).launch {
            val key: String = RedisKeys.getSnsHashKey(id = sns.id!!)
            val snsCache: SnsCache = sns.toCacheDto()

            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
        }
    }

    fun delete(id: Long) {
        snsCommandService.delete(id = id)

        CoroutineScope(context = Dispatchers.IO).launch {
            val key: String = RedisKeys.getSnsHashKey(id = id)

            snsCacheRepository.zremString(key = SNS_ZSET_KEY, value = key)
        }
    }
}

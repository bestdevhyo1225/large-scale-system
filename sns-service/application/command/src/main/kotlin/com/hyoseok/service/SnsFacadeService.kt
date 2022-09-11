package com.hyoseok.service

import com.hyoseok.config.RedisExpireTimes.SNS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.SNS_ZSET_KEY
import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.sns.entity.SnsCache
import com.hyoseok.sns.repository.SnsCacheRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
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
    private val snsReadRepository: SnsReadRepository,
) {

    fun create(dto: SnsCreateDto): SnsCreateResultDto {
        val snsCreateResultDto = snsCommandService.create(dto = dto)

        CoroutineScope(context = Dispatchers.IO).launch {
            val key = RedisKeys.getSnsKey(id = snsCreateResultDto.snsId)
            val snsCache: SnsCache = snsReadRepository.findWithAssociatedEntitiesById(snsId = snsCreateResultDto.snsId)
                .toCacheDto()

            // 트랜잭션 처리가 필요한가?
            snsCacheRepository.setex(key = key, value = snsCache, expireTime = SNS, timeUnit = SECONDS)
            snsCacheRepository.zaddSnsKeys(
                key = SNS_ZSET_KEY,
                value = key,
                score = Timestamp.valueOf(snsCache.createdAt).time.toDouble(),
            )
        }

        return snsCreateResultDto
    }
}

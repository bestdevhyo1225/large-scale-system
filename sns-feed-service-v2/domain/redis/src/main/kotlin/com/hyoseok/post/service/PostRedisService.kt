package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostCache.Companion.POST_CACHE_EXPIRE_TIME
import com.hyoseok.post.entity.PostCache.Companion.POST_IDS_CACHE_BY_MEMBER_ID_EXPIRE_TIME
import com.hyoseok.post.entity.PostCache.Companion.getPostIdKey
import com.hyoseok.post.entity.PostCache.Companion.getPostIdsByMemberIdKey
import com.hyoseok.post.repository.PostRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisRepository: PostRedisRepository,
) {

    fun createOrUpdate(dto: PostCacheDto) {
        val postCache: PostCache = dto.toEntity()
        postRedisRepository.set(
            key = getPostIdKey(id = postCache.id),
            value = postCache,
            expireTime = POST_CACHE_EXPIRE_TIME,
            timeUnit = SECONDS,
        )
        postRedisRepository.zaddAndExpire(
            key = getPostIdsByMemberIdKey(memberId = postCache.memberId),
            value = postCache.id,
            score = postCache.id.toDouble(),
            expireTime = POST_IDS_CACHE_BY_MEMBER_ID_EXPIRE_TIME,
            timeUnit = SECONDS,
        )
    }
}

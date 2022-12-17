package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache.Companion.getPostViewsKeyAndExpireTime
import com.hyoseok.post.entity.PostCache.Companion.getPostWishesKeyAndExpireTime
import com.hyoseok.post.repository.PostRedisRepository
import com.hyoseok.post.repository.PostRedisTransactionRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisRepository: PostRedisRepository,
    private val postRedisTransactionRepository: PostRedisTransactionRepository,
) {

    fun createOrUpdate(dto: PostCacheDto) {
        with(receiver = dto) {
            postRedisTransactionRepository.createPostCache(postCache = toEntity())

            val (postViewKey: String, postViewExpireTime: Long) = getPostViewsKeyAndExpireTime(id = id)
            val (postWishKey: String, postWishExpireTime: Long) = getPostWishesKeyAndExpireTime(id = id)

            postRedisRepository.set(
                key = postViewKey,
                value = viewCount,
                expireTime = postViewExpireTime,
                timeUnit = SECONDS,
            )
            postRedisRepository.set(
                key = postWishKey,
                value = wishCount,
                expireTime = postWishExpireTime,
                timeUnit = SECONDS,
            )
        }
    }
}

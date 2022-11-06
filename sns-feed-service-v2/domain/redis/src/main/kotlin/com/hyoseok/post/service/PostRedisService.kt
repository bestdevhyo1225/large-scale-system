package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImageCache
import com.hyoseok.post.repository.PostRedisTransactionRepository
import com.hyoseok.post.repository.PostRedisRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.SECONDS

@Service
@ConditionalOnProperty(prefix = "spring.post.redis", name = ["enable"], havingValue = "true")
class PostRedisService(
    private val postRedisRepository: PostRedisRepository,
    private val postRedisTransactionRepository: PostRedisTransactionRepository,
) {

    fun create(dto: PostCacheDto) {
        createPostCache(dto = dto)
        createPostViewCache(postId = dto.id, viewCount = dto.viewCount)
    }

    private fun createPostCache(dto: PostCacheDto) {
        val postCache: PostCache = with(receiver = dto) {
            PostCache(
                id = id,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = images.map { PostImageCache(id = it.id, url = it.url, sortOrder = it.sortOrder) },
            )
        }
        postRedisTransactionRepository.createPostCache(postCache = postCache)
    }

    private fun createPostViewCache(postId: Long, viewCount: Long) {
        val (key: String, expireTime: Long) = PostCache.getPostViewKeyAndExpireTime(id = postId)
        postRedisRepository.set(key = key, value = viewCount, expireTime = expireTime, timeUnit = SECONDS)
    }

    fun incrementPostView(postId: Long) {
        postRedisRepository.increment(key = PostCache.getPostIdViewsKey(id = postId))
    }
}

package com.hyoseok.service.post

import com.hyoseok.config.RedisCommons.ZSET_MAX_LIMIT
import com.hyoseok.config.RedisExpireTimes.POST
import com.hyoseok.config.RedisExpireTimes.POST_VIEWS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.POST_KEYS
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostCacheRepository
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.service.dto.PostCreateDto
import com.hyoseok.service.dto.PostCreateResultDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val postCacheRepository: PostCacheRepository,
) {

    fun create(dto: PostCreateDto): PostCreateResultDto {
        val post: Post = dto.toEntity()

        postRepository.save(post = post)

        CoroutineScope(context = Dispatchers.IO).launch {
            setPostCache(id = post.id!!, postCache = post.toPostCache())
            setPostViewCache(id = post.id!!, viewCount = post.viewCount)
            zaddPostKeys(id = post.id!!, createdAt = post.createdAt)
            zremPostKeysRangeByRank()
        }

        return PostCreateResultDto(post = post)
    }

    private suspend fun setPostCache(id: Long, postCache: PostCache) {
        postCacheRepository.set(
            key = RedisKeys.getPostKey(id = id),
            value = postCache,
            expireTime = POST,
            timeUnit = SECONDS,
        )
    }

    private suspend fun setPostViewCache(id: Long, viewCount: Long) {
        postCacheRepository.set(
            key = RedisKeys.getPostViewsKey(id = id),
            value = viewCount,
            expireTime = POST_VIEWS,
            timeUnit = SECONDS,
        )
    }

    private suspend fun zaddPostKeys(id: Long, createdAt: LocalDateTime) {
        postCacheRepository.zadd(
            key = POST_KEYS,
            value = id,
            score = Timestamp.valueOf(createdAt).time.toDouble(),
        )
    }

    private suspend fun zremPostKeysRangeByRank() {
        postCacheRepository.zremRangeByRank(key = POST_KEYS, start = ZSET_MAX_LIMIT, end = ZSET_MAX_LIMIT)
    }
}

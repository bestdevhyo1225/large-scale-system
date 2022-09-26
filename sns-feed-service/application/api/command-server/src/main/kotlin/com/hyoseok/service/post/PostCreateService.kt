package com.hyoseok.service.post

import com.hyoseok.config.KafkaTopics
import com.hyoseok.config.RedisCommons.ZSET_POST_MAX_LIMIT
import com.hyoseok.config.RedisExpireTimes.POST
import com.hyoseok.config.RedisExpireTimes.POST_VIEWS
import com.hyoseok.config.RedisKeys
import com.hyoseok.config.RedisKeys.POST_KEYS
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowReadRepository
import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.repository.PostCacheRepository
import com.hyoseok.post.repository.PostRepository
import com.hyoseok.publisher.KafkaProducer
import com.hyoseok.service.dto.FollowerSendEventDto
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
class PostCreateService(
    private val postRepository: PostRepository,
    private val postCacheRepository: PostCacheRepository,
    private val followReadRepository: FollowReadRepository,
    private val memberReadRepository: MemberReadRepository,
    private val kafkaProducer: KafkaProducer,
) {

    fun execute(dto: PostCreateDto): PostCreateResultDto {
        val post: Post = dto.toEntity()

        if (memberReadRepository.exists(id = dto.memberId)) {
            postRepository.save(post = post)
        }

        CoroutineScope(context = Dispatchers.IO).launch {
            launch { setPostCache(id = post.id!!, postCache = post.toPostCache()) }
            launch { setPostViewCount(id = post.id!!, viewCount = post.viewCount) }
            launch { zaddPostKeys(id = post.id!!, createdAt = post.createdAt) }
            launch { zremPostKeysRangeByRank() }
            launch {
                findFollowerAndSendFeed(postId = post.id!!, createdAt = post.createdAt, followeeId = dto.memberId)
            }
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

    private suspend fun setPostViewCount(id: Long, viewCount: Long) {
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
        postCacheRepository.zremRangeByRank(key = POST_KEYS, start = ZSET_POST_MAX_LIMIT, end = ZSET_POST_MAX_LIMIT)
    }

    private suspend fun findFollowerAndSendFeed(
        postId: Long,
        createdAt: LocalDateTime,
        followeeId: Long,
    ) {
        val limit = 1000L
        var offset = 0L
        var isProgress = true
        while (isProgress) {
            val (total: Long, follows: List<Follow>) = findAllByFolloweeIdAndLimitAndOffset(
                followeeId = followeeId,
                limit = limit,
                offset = offset,
            )

            follows.forEach { sendFeedToFollower(postId = postId, createdAt = createdAt, followerId = it.followerId) }

            offset += limit

            if (offset >= total) {
                isProgress = false
            }
        }
    }

    private suspend fun findAllByFolloweeIdAndLimitAndOffset(
        followeeId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> =
        followReadRepository.findAllByFolloweeIdAndLimitAndOffset(
            followeeId = followeeId,
            limit = limit,
            offset = offset,
        )

    private suspend fun sendFeedToFollower(postId: Long, createdAt: LocalDateTime, followerId: Long) {
        kafkaProducer.send(
            event = FollowerSendEventDto(postId = postId, createdAt = createdAt, followerId = followerId),
            topic = KafkaTopics.SNS_FEED,
        )
    }
}

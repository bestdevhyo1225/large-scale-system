package com.hyoseok.feed.service

import com.hyoseok.feed.dto.FeedCacheDto
import com.hyoseok.feed.entity.FeedCache.Companion.getMemberIdFeedsKey
import com.hyoseok.feed.repository.FeedRedisRepository
import com.hyoseok.util.PageRequestByPosition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisReadService(
    private val feedRedisRepository: FeedRedisRepository,
) {

    fun findFeeds(memberId: Long, pageRequestByPosition: PageRequestByPosition): List<FeedCacheDto> {
        val (start: Long, size: Long) = pageRequestByPosition
        val key: String = getMemberIdFeedsKey(id = memberId)
        val end: Long = start.plus(size).minus(other = 1)

        return feedRedisRepository
            .zrevRange(key = key, start = start, end = end, clazz = Long::class.java)
            .map { FeedCacheDto(memberId = memberId, postId = it) }
    }
}

package com.hyoseok.feed.service

import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.entity.Feed
import com.hyoseok.feed.repository.FeedRedisRepository
import com.hyoseok.util.PageRequestByPosition
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisReadService(
    private val feedRedisRepository: FeedRedisRepository,
) {

    fun findFeeds(memberId: Long, pageRequestByPosition: PageRequestByPosition): List<FeedDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (start <= -1L || size == 0L) {
            return listOf()
        }

        val key: String = Feed.getMemberIdFeedsKey(id = memberId)
        val end: Long = start.plus(size).minus(other = 1)

        return feedRedisRepository
            .zrevRange(key = key, start = start, end = end, clazz = Long::class.java)
            .map { FeedDto(memberId = memberId, postId = it) }
    }
}

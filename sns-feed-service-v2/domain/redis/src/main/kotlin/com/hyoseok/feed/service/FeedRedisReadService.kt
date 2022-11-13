package com.hyoseok.feed.service

import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.entity.Feed
import com.hyoseok.feed.repository.FeedRedisRepository
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import com.hyoseok.util.PageRequestByPosition.Companion.NONE_START
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "spring.feed.redis", name = ["enable"], havingValue = "true")
class FeedRedisReadService(
    private val feedRedisRepository: FeedRedisRepository,
) {

    fun findFeeds(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<FeedDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (size == 0L) {
            return PageByPosition(items = listOf(), nextPageRequestByPosition = PageRequestByPosition())
        }

        if (start <= NONE_START) {
            return PageByPosition(
                items = listOf(),
                nextPageRequestByPosition = pageRequestByPosition.next(start = NONE_START),
            )
        }

        val key: String = Feed.getMemberIdFeedsKey(id = memberId)
        val end: Long = start.plus(size).minus(other = 1)
        val feedDtos: List<FeedDto> = feedRedisRepository
            .zrevRange(key = key, start = start, end = end, clazz = Long::class.java)
            .map { FeedDto(memberId = memberId, postId = it) }
        val nextStart: Long = if (feedDtos.isEmpty()) NONE_START else start.plus(size)

        return PageByPosition(
            items = feedDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(start = nextStart),
        )
    }
}

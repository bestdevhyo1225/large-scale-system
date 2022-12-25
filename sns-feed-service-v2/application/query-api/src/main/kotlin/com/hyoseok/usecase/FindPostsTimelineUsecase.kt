package com.hyoseok.usecase

import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsTimelineUsecase(
    private val feedRedisReadService: FeedRedisReadService,
    private val followReadService: FollowReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    //    @RateLimiter(name = FIND_POST_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (start < 0 || size == 0L) {
            return PageByPosition(items = listOf(), nextPageRequestByPosition = pageRequestByPosition)
        }

        val feedDtos: List<FeedDto> =
            feedRedisReadService.findFeeds(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        if (feedDtos.isEmpty()) {
            return findInfluencerPosts(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        }

        val postIds: List<Long> = feedDtos.map { it.postId }
        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)
        val postDtos: List<PostDto> = postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun findInfluencerPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): PageByPosition<PostDto> {
        val findFolloweeMaxLimit: Long = 1_000
        val followeeIds: List<Long> = followReadService.findInfluencerFolloweeIds(
            followerId = memberId,
            findFolloweeMaxLimit = findFolloweeMaxLimit,
        )

        // followeeIds 들이 등록한 PostCache를 가져올 수 있는 방법

        val postDtos: List<PostDto> =
            postReadService.findPosts(memberIds = followeeIds, pageRequestByPosition = pageRequestByPosition)

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostCacheDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

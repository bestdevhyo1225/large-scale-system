package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostTimelineUsecase(
    private val followReadService: FollowReadService,
    private val feedRedisReadService: FeedRedisReadService,
    private val memberReadService: MemberReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> = runBlocking {
        val (
            feedPostsRequestByPosition: PageRequestByPosition,
            influencerPostsRequestByPosition: PageRequestByPosition,
        ) = pageRequestByPosition.splitTwoPageRequestByPosition()

        val deferredFeedPosts: Deferred<List<PostDto>> = async(context = Dispatchers.IO) {
            findFeedPosts(memberId = memberId, pageRequestByPosition = feedPostsRequestByPosition)
        }
        val deferredInfluencerPosts: Deferred<List<PostDto>> = async(context = Dispatchers.IO) {
            findInfluencerPosts(memberId = memberId, pageRequestByPosition = influencerPostsRequestByPosition)
        }
        val postDtos: List<PostDto> = deferredFeedPosts.await().plus(deferredInfluencerPosts.await())

        PageByPosition(
            items = postDtos.shuffled(),
            nextPageRequestByPosition = PageRequestByPosition(
                start = feedPostsRequestByPosition.start.plus(feedPostsRequestByPosition.size),
                size = feedPostsRequestByPosition.size,
            ),
        )
    }

    private suspend fun findFeedPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): List<PostDto> {
        val feedDtos: List<FeedDto> =
            feedRedisReadService.findFeeds(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        val postIds: List<Long> = feedDtos.map { it.postId }
        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)
        return postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }
    }

    private suspend fun findInfluencerPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): List<PostDto> {
        val followeeIds: List<Long> = followReadService.findFolloweeIdsByStaticLimit(followerId = memberId)
        val memberDtos: List<MemberDto> = memberReadService.findInfluencerMembers(ids = followeeIds)
        val memberIds: List<Long> = memberDtos.map { it.id }
        return postReadService.findPosts(memberIds = memberIds, pageRequestByPosition = pageRequestByPosition)
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

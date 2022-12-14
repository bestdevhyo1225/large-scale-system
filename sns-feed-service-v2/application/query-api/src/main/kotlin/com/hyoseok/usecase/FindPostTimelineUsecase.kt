package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.usecase.dto.FindPostWishUsecaseDto
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import com.hyoseok.wish.service.WishReadService
import com.hyoseok.wish.service.WishRedisReadService
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
    private val wishRedisReadService: WishRedisReadService,
    private val wishReadService: WishReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): PageByPosition<FindPostWishUsecaseDto> = runBlocking {
        val (
            feedPostsRequestByPosition: PageRequestByPosition,
            influencerPostsRequestByPosition: PageRequestByPosition,
        ) = pageRequestByPosition.splitTwoPageRequestByPosition()

        val deferredFeedPosts: Deferred<List<PostDto>> = async(context = Dispatchers.IO) {
            getFeedPosts(memberId = memberId, pageRequestByPosition = feedPostsRequestByPosition)
        }
        val deferredInfluencerPosts: Deferred<List<PostDto>> = async(context = Dispatchers.IO) {
            getInfluencerPosts(memberId = memberId, pageRequestByPosition = influencerPostsRequestByPosition)
        }
        val postDtos: List<PostDto> = deferredFeedPosts.await().plus(deferredInfluencerPosts.await())
        val wishCountsMap: Map<Long, Long> = getWishCounts(postDtos = postDtos)
        val findPostWishUsecaseDtos: List<FindPostWishUsecaseDto> = postDtos.map {
            FindPostWishUsecaseDto(postDto = it, wishCount = wishCountsMap[it.id] ?: 0L)
        }

        PageByPosition(
            items = findPostWishUsecaseDtos.shuffled(),
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = findPostWishUsecaseDtos.size),
        )
    }

    private suspend fun getFeedPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): List<PostDto> {
        val feedDtos: List<FeedDto> = feedRedisReadService.findFeeds(
            memberId = memberId,
            pageRequestByPosition = pageRequestByPosition,
        )
        val postIds: List<Long> = feedDtos.map { it.postId }
        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)
        return postCacheDtos.map { postCacheDto ->
            with(receiver = postCacheDto) {
                PostDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }
        }
    }

    private suspend fun getInfluencerPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): List<PostDto> {
        val followeeIds: List<Long> = followReadService.findFolloweeIdsByStaticLimit(followerId = memberId)
        val memberDtos: List<MemberDto> = memberReadService.findInfluencerMembers(ids = followeeIds)
        val memberIds: List<Long> = memberDtos.map { it.id }
        return postReadService.findPosts(memberIds = memberIds, pageRequestByPosition = pageRequestByPosition)
    }

    private fun getWishCounts(postDtos: List<PostDto>): Map<Long, Long> {
        val postIds: List<Long> = postDtos.map { it.id }
        val wishCountsByRedis: Map<Long, Long> = wishRedisReadService.findWishCounts(postIds = postIds)
        if (wishCountsByRedis.isNotEmpty() && wishCountsByRedis.size == postIds.size) {
            return wishCountsByRedis
        }
        return wishReadService.getCountsByPostIds(postIds = postIds)
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<FindPostWishUsecaseDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

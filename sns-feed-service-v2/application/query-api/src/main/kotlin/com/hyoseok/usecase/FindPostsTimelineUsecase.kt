package com.hyoseok.usecase

import com.hyoseok.config.resilience4j.ratelimiter.RateLimiterConfig.Name.FIND_POST_TIMELINE_USECASE
import com.hyoseok.exception.QueryApiRateLimitException
import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.mapper.PostCacheDtoMapper
import com.hyoseok.mapper.PostDtoMapper
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class FindPostsTimelineUsecase(
    private val feedRedisReadService: FeedRedisReadService,
    private val followReadService: FollowReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postRedisService: PostRedisService,
    private val postReadService: PostReadService,
) {

    private val logger = KotlinLogging.logger {}

    @RateLimiter(name = FIND_POST_TIMELINE_USECASE, fallbackMethod = "fallbackExecute")
    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val feedDtos: List<FeedDto> =
            feedRedisReadService.findFeeds(memberId = memberId, pageRequestByPosition = pageRequestByPosition)

        if (feedDtos.isEmpty()) {
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

        val postIds: List<Long> = feedDtos.map { it.postId }
        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)
        val postDtos: List<PostDto> =
            if (postCacheDtos.isNotEmpty() && postCacheDtos.size == pageRequestByPosition.size.toInt()) {
                postCacheDtos.map { PostDtoMapper.of(postCacheDto = it) }
            } else {
                findPostAndCreatePostCache(postCacheDtos = postCacheDtos, postIds = postIds)
            }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    private fun findPostAndCreatePostCache(
        postCacheDtos: List<PostCacheDto>,
        postIds: List<Long>,
    ): List<PostDto> {
        val postIdMapByPostCacheDtos: Map<Long, Boolean> = postCacheDtos.associate { it.id to true }
        val postDtos: List<PostDto> = postReadService.findPosts(ids = postIds)
        val createPostCacheDto: List<PostCacheDto> = postDtos
            .filter { postIdMapByPostCacheDtos[it.id] == null || postIdMapByPostCacheDtos[it.id] == false }
            .map { PostCacheDtoMapper.of(postDto = it) }

        CoroutineScope(context = Dispatchers.IO).launch {
            createPostCacheDto.forEach { postRedisService.createOrUpdate(dto = it) }
        }

        return postDtos
    }

    private fun fallbackExecute(exception: Exception): PageByPosition<PostDto> {
        logger.error { exception.localizedMessage }
        throw QueryApiRateLimitException(message = "일시적으로 타임라인을 조회할 수 없습니다. 잠시 후에 다시 시도해주세요.")
    }
}

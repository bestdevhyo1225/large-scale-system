package com.hyoseok.usecase

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
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class FindPostTimelineUsecase(
    private val followReadService: FollowReadService,
    private val feedRedisReadService: FeedRedisReadService,
    private val memberReadService: MemberReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> = runBlocking {
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
        val items: List<PostDto> = deferredFeedPosts.await().plus(deferredInfluencerPosts.await())

        PageByPosition(
            items = items.shuffled(),
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = items.size),
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

        if (postCacheDtos.isNotEmpty()) {
            return postCacheDtos.map { createPostDto(postCacheDto = it) }
        }

        return postReadService.findPosts(ids = postIds)
    }

    private suspend fun getInfluencerPosts(
        memberId: Long,
        pageRequestByPosition: PageRequestByPosition,
    ): List<PostDto> {
        val followeeIds: List<Long> = followReadService.findFolloweeIdsByStaticLimit(followerId = memberId)
        val memberDtos: List<MemberDto> = memberReadService.findInfluencerMembers(ids = followeeIds)
        return postReadService.findPosts(
            memberIds = memberDtos.map { it.id },
            pageRequestByPosition = pageRequestByPosition,
        )
    }

    private fun createPostDto(postCacheDto: PostCacheDto): PostDto =
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

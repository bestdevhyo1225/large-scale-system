package com.hyoseok.usecase

import com.hyoseok.feed.dto.FeedDto
import com.hyoseok.feed.service.FeedRedisReadService
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import org.springframework.stereotype.Service

@Service
class FindPostTimelineUsecase(
    private val feedRedisReadService: FeedRedisReadService,
    private val postRedisReadService: PostRedisReadService,
    private val postReadService: PostReadService,
) {

    fun execute(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (feedDtos: List<FeedDto>, nextPageRequestByPosition: PageRequestByPosition) =
            feedRedisReadService.findFeeds(memberId = memberId, pageRequestByPosition = pageRequestByPosition)
        val postIds: List<Long> = feedDtos.map { it.postId }

        val postCacheDtos: List<PostCacheDto> = postRedisReadService.findPostCaches(ids = postIds)

        if (postCacheDtos.isNotEmpty()) {
            return PageByPosition(
                items = postCacheDtos.map { createPostDto(postCacheDto = it) },
                nextPageRequestByPosition = nextPageRequestByPosition,
            )
        }

        val postDtos: List<PostDto> = postReadService.findPosts(ids = postIds)

        return PageByPosition(items = postDtos, nextPageRequestByPosition = nextPageRequestByPosition)
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

package com.hyoseok.usecase

import com.hyoseok.follow.service.FollowReadService
import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.service.MemberReadService
import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostCreateDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.service.PostRedisService
import com.hyoseok.post.service.PostService
import com.hyoseok.feed.producer.FeedKafkaProducer
import com.hyoseok.feed.dto.FeedEventDto
import com.hyoseok.usecase.dto.CreatePostUsecaseDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class CreatePostUsecase(
    private val postService: PostService,
    private val postRedisService: PostRedisService,
    private val followReadService: FollowReadService,
    private val memberReadService: MemberReadService,
    private val feedKafkaProducer: FeedKafkaProducer,
) {

    fun execute(createPostUsecaseDto: CreatePostUsecaseDto): PostDto {
        val memberDto: MemberDto = memberReadService.findMember(id = createPostUsecaseDto.memberId)
        val postDto: PostDto = postService.create(
            dto = PostCreateDto(
                memberId = memberDto.id,
                writer = memberDto.name,
                title = createPostUsecaseDto.title,
                contents = createPostUsecaseDto.contents,
                images = createPostUsecaseDto.images,
            ),
        )

        CoroutineScope(context = Dispatchers.IO).launch {
            createPostCache(postDto = postDto)
            sendFeedEvent(postId = postDto.id, followeeId = postDto.memberId)
        }

        return postDto
    }

    private suspend fun createPostCache(postDto: PostDto) {
        with(receiver = postDto) {
            postRedisService.create(
                dto = PostCacheDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                ),
            )
        }
    }

    private suspend fun sendFeedEvent(postId: Long, followeeId: Long) {
        val limit = 1_000L
        var offset = 0L
        var isProgress = true
        while (isProgress) {
            val (total: Long, followerIds: List<Long>) = followReadService.findFollowerIds(
                followeeId = followeeId,
                limit = limit,
                offset = offset,
            )

            followerIds.forEach {
                feedKafkaProducer.sendAsync(FeedEventDto(postId = postId, followerId = it))
            }

            offset += limit

            if (offset >= total) {
                isProgress = false
            }
        }
    }
}

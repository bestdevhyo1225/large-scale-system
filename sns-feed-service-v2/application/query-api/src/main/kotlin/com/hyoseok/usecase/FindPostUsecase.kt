package com.hyoseok.usecase

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto
import com.hyoseok.post.dto.PostImageDto
import com.hyoseok.post.service.PostReadService
import com.hyoseok.post.service.PostRedisReadService
import com.hyoseok.post.service.PostRedisService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class FindPostUsecase(
    private val postRedisReadService: PostRedisReadService,
    private val postRedisService: PostRedisService,
    private val postReadService: PostReadService,
) {

    fun execute(postId: Long): PostDto {
        postRedisReadService.findPostCache(id = postId)?.let { return createPostDto(postCacheDto = it) }

        val postDto: PostDto = postReadService.findPost(id = postId)

        CoroutineScope(context = Dispatchers.IO).launch {
            postRedisService.create(dto = createPostCacheDto(postDto = postDto))
        }

        return postDto
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

    private fun createPostCacheDto(postDto: PostDto): PostCacheDto =
        with(receiver = postDto) {
            PostCacheDto(
                id = id,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
            )
        }
}

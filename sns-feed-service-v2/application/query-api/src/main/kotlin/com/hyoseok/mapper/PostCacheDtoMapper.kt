package com.hyoseok.mapper

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageCacheDto

object PostCacheDtoMapper {
    fun of(postDto: PostDto): PostCacheDto {
        return with(receiver = postDto) {
            PostCacheDto(
                id = id,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                viewCount = viewCount,
                wishCount = wishCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = images.map { PostImageCacheDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
            )
        }
    }
}

package com.hyoseok.mapper

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto

object PostDtoMapper {
    fun of(postCacheDto: PostCacheDto): PostDto {
        return with(receiver = postCacheDto) {
            PostDto(
                id = id,
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                wishCount = wishCount,
                createdAt = createdAt,
                updatedAt = updatedAt,
                images = images.map {
                    PostImageDto(
                        id = it.id,
                        url = it.url,
                        sortOrder = it.sortOrder,
                    )
                },
            )
        }
    }
}

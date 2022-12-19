package com.hyoseok.post.dto

import com.hyoseok.post.entity.Post
import java.time.LocalDateTime

data class PostDto(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val wishCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImageDto>,
) {
    companion object {
        operator fun invoke(post: Post): PostDto =
            with(receiver = post) {
                PostDto(
                    id = id!!,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    wishCount = wishCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = postImages.map {
                        PostImageDto(
                            id = it.id!!,
                            url = it.url,
                            sortOrder = it.sortOrder,
                        )
                    },
                )
            }
    }
}

data class PostImageDto(
    val id: Long,
    val url: String,
    val sortOrder: Int,
)

data class PostCreateDto(
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<PostImageCreateDto>,
)

data class PostImageCreateDto(
    val url: String,
    val sortOrder: Int,
)

package com.hyoseok.post.dto

import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImageCache
import java.time.LocalDateTime

data class PostCacheDto(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val viewCount: Long,
    val wishCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImageCacheDto>,
) {

    fun toEntity(): PostCache =
        PostCache(
            id = id,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            viewCount = viewCount,
            wishCount = wishCount,
            createdAt = createdAt,
            updatedAt = updatedAt,
            images = images.map { PostImageCache(id = it.id, url = it.url, sortOrder = it.sortOrder) },
        )

    companion object {
        operator fun invoke(postCache: PostCache): PostCacheDto =
            with(receiver = postCache) {
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
                    images = images.map {
                        PostImageCacheDto(
                            id = it.id,
                            url = it.url,
                            sortOrder = it.sortOrder,
                        )
                    },
                )
            }
    }
}

data class PostImageCacheDto(
    val id: Long,
    val url: String,
    val sortOrder: Int,
)

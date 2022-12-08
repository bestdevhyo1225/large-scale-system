package com.hyoseok.usecase.dto

import com.hyoseok.post.dto.PostCacheDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.dto.PostImageDto
import java.time.LocalDateTime

data class FindPostWishUsecaseDto(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val viewCount: Long,
    val wishCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImageDto>,
) {
    companion object {
        operator fun invoke(postDto: PostDto, wishCount: Long): FindPostWishUsecaseDto =
            with(receiver = postDto) {
                FindPostWishUsecaseDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    wishCount = wishCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }

        operator fun invoke(postCacheDto: PostCacheDto, wishCount: Long): FindPostWishUsecaseDto =
            with(receiver = postCacheDto) {
                FindPostWishUsecaseDto(
                    id = id,
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    wishCount = wishCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images.map { PostImageDto(id = it.id, url = it.url, sortOrder = it.sortOrder) },
                )
            }
    }
}

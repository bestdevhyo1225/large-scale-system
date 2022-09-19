package com.hyoseok.service.dto

import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage

data class PostCreateDto(
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<PostImageCreateDto>,
) {
    fun toEntity() =
        Post(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toEntity() },
        )
}

data class PostImageCreateDto(
    val url: String,
    val sortOrder: Int,
) {
    fun toEntity() = PostImage(url = url, sortOrder = sortOrder)
}

data class PostCreateResultDto(
    val postId: Long,
) {
    companion object {
        operator fun invoke(post: Post) =
            with(receiver = post) { PostCreateResultDto(postId = id!!) }
    }
}

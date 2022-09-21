package com.hyoseok.service.dto

import com.hyoseok.member.entity.Member
import com.hyoseok.post.entity.PostCache
import com.hyoseok.post.entity.PostImage
import java.time.LocalDateTime

data class PostFindResultDto(
    val postId: Long,
    val memberId: Long,
    val memberName: String,
    val title: String,
    val contents: String,
    val writer: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImage>,
) {
    companion object {
        operator fun invoke(postCache: PostCache, member: Member) =
            with(receiver = postCache) {
                PostFindResultDto(
                    postId = id,
                    memberId = memberId,
                    memberName = member.name,
                    title = title,
                    contents = contents,
                    writer = writer,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    images = images,
                )
            }
    }
}

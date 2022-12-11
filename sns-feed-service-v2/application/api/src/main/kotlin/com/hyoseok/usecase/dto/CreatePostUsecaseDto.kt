package com.hyoseok.usecase.dto

import com.hyoseok.member.dto.MemberDto
import com.hyoseok.post.dto.PostCreateDto
import com.hyoseok.post.dto.PostImageCreateDto

data class CreatePostUsecaseDto(
    val memberId: Long,
    val title: String,
    val contents: String,
    val images: List<PostImageCreateDto>,
) {

    fun toDomainDto(memberDto: MemberDto): PostCreateDto =
        PostCreateDto(
            memberId = memberDto.id,
            writer = memberDto.name,
            title = title,
            contents = contents,
            images = images,
        )
}

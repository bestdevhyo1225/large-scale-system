package com.hyoseok.usecase.dto

import com.hyoseok.post.dto.PostImageCreateDto

data class CreatePostUsecaseDto(
    val memberId: Long,
    val title: String,
    val contents: String,
    val images: List<PostImageCreateDto>,
)

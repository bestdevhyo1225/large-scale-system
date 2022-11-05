package com.hyoseok.controller.dto

import com.hyoseok.post.dto.PostImageCreateDto
import com.hyoseok.usecase.dto.CreatePostUsecaseDto
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PositiveOrZero

data class PostCreateRequestDto(
    @field:NotBlank(message = "title을 입력하세요")
    @field:Schema(description = "게시글 제목", example = "제목", required = true)
    val title: String,

    @field:NotBlank(message = "contents를 입력하세요")
    @field:Schema(description = "게시글 내용", example = "내용", required = true)
    val contents: String,

    @field:NotEmpty(message = "images는 비어 있을 수 없습니다")
    val images: List<@Valid PostImageCreateRequest>,
) {
    fun toUsecaseDto(memberId: Long): CreatePostUsecaseDto =
        CreatePostUsecaseDto(
            memberId = memberId,
            title = title,
            contents = contents,
            images = images.map { PostImageCreateDto(url = it.url, sortOrder = it.sortOrder) },
        )
}

data class PostImageCreateRequest(
    @field:NotBlank(message = "url을 입력하세요")
    @field:Schema(description = "게시글 이미지 경로", example = "https://hyo-test.com/images", required = true)
    val url: String,

    @field:PositiveOrZero(message = "sortOrder는 음수를 입력할 수 없습니다")
    @field:Schema(description = "게시글 이미지 순서", example = "1", required = true)
    val sortOrder: Int,
)

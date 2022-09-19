package com.hyoseok.web.request

import com.hyoseok.service.dto.PostCreateDto
import com.hyoseok.service.dto.PostImageCreateDto
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class PostCreateRequest(
    @field:Positive(message = "memberId는 0보다 큰 값을 입력하세요")
    val memberId: Long,

    @field:NotBlank(message = "title을 입력하세요")
    val title: String,

    @field:NotBlank(message = "contents를 입력하세요")
    val contents: String,

    @field:NotBlank(message = "writer를 입력하세요")
    val writer: String,

    @field:NotEmpty(message = "images는 비어 있을 수 없습니다")
    val images: List<@Valid PostImageCreateRequest>,
) {
    fun toServiceDto() =
        PostCreateDto(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toServiceDto() },
        )
}

data class PostImageCreateRequest(
    @field:NotBlank(message = "url을 입력하세요")
    val url: String,

    @field:PositiveOrZero(message = "sortOrder는 음수를 입력할 수 없습니다")
    val sortOrder: Int,
) {
    fun toServiceDto() = PostImageCreateDto(url = url, sortOrder = sortOrder)
}

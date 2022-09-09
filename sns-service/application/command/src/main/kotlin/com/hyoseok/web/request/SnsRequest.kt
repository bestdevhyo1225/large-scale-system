package com.hyoseok.web.request

import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsEditDto
import com.hyoseok.service.dto.SnsImageDto
import com.hyoseok.service.dto.SnsTagDto
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class SnsCreateRequest(

    @field:NotBlank(message = "title을 입력하세요")
    val title: String,

    @field:NotBlank(message = "contents를 입력하세요")
    val contents: String,

    @field:NotBlank(message = "writer를 입력하세요")
    val writer: String,

    @field:NotEmpty(message = "images는 비어 있을 수 없습니다")
    val images: List<@Valid SnsImageRequest>,

    @field:NotBlank(message = "tagType을 입력하세요")
    val tagType: String,

    @field:NotEmpty(message = "tagValues는 비어 있을 수 없습니다")
    val tagValues: List<@Valid String>,
) {
    fun toServiceDto() =
        SnsCreateDto(
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toServiceDto() },
            tag = SnsTagDto(type = tagType, values = tagValues),
        )
}

data class SnsEditRequest(

    @field:Positive(message = "id는 0보다 큰 값을 입력하세요")
    val id: Long,

    @field:NotBlank(message = "title을 입력하세요")
    val title: String,

    @field:NotBlank(message = "contents를 입력하세요")
    val contents: String,

    @field:NotBlank(message = "writer를 입력하세요")
    val writer: String,

    @field:NotEmpty(message = "images는 비어 있을 수 없습니다")
    val images: List<@Valid SnsImageRequest>,

    @field:NotBlank(message = "tagType을 입력하세요")
    val tagType: String,

    @field:NotEmpty(message = "tagValues는 비어 있을 수 없습니다")
    val tagValues: List<@Valid String>,
) {
    fun toServiceDto() =
        SnsEditDto(
            id = id,
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toServiceDto() },
            tag = SnsTagDto(type = tagType, values = tagValues),
        )
}

data class SnsImageRequest(

    @field:NotBlank(message = "url을 입력하세요")
    val url: String,

    @field:PositiveOrZero(message = "id는 0과 같거나 큰 값을 입력하세요")
    val sortOrder: Int,
) {
    fun toServiceDto() = SnsImageDto(url = url, sortOrder = sortOrder)
}

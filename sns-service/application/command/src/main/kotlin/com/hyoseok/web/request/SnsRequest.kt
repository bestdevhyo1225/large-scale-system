package com.hyoseok.web.request

import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsEditDto
import com.hyoseok.service.dto.SnsImageDto
import com.hyoseok.service.dto.SnsTagCreateDto
import com.hyoseok.service.dto.SnsTagEditDto
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class SnsCreateRequest(

    @field:Positive(message = "memberId는 0보다 큰 값을 입력하세요")
    val memberId: Long,

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

    @field:NotEmpty(message = "productIds 비어 있을 수 없습니다")
    val productIds: List<@Valid Long>,
) {
    fun toServiceDto() =
        SnsCreateDto(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toServiceDto() },
            tag = SnsTagCreateDto(type = tagType, values = tagValues),
            productIds = productIds,
        )
}

data class SnsEditRequest(

    @field:Positive(message = "memberId는 0보다 큰 값을 입력하세요")
    val memberId: Long,

    @field:NotBlank(message = "title을 입력하세요")
    val title: String,

    @field:NotBlank(message = "contents를 입력하세요")
    val contents: String,

    @field:NotBlank(message = "writer를 입력하세요")
    val writer: String,

    @field:NotEmpty(message = "images는 비어 있을 수 없습니다")
    val images: List<@Valid SnsImageRequest>,

    @field:Positive(message = "tagId는 0보다 큰 값을 입력하세요")
    val tagId: Long,

    @field:NotBlank(message = "tagType을 입력하세요")
    val tagType: String,

    @field:NotEmpty(message = "tagValues는 비어 있을 수 없습니다")
    val tagValues: List<@Valid String>,

    @field:NotEmpty(message = "productIds 비어 있을 수 없습니다")
    val productIds: List<@Valid Long>,
) {
    fun toServiceDto(id: Long) =
        SnsEditDto(
            id = id,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            images = images.map { it.toServiceDto() },
            tag = SnsTagEditDto(id = tagId, type = tagType, values = tagValues),
            productIds = productIds,
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

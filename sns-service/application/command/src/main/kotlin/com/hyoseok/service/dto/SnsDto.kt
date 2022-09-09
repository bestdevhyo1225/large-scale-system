package com.hyoseok.service.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.entity.SnsImage
import com.hyoseok.sns.entity.SnsTag
import java.time.LocalDateTime

data class SnsCreateDto(
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<SnsImageDto>,
    val tag: SnsTagDto,
) {
    fun toEntity() =
        Sns(
            title = title,
            contents = contents,
            writer = writer,
            snsImages = SnsImageDto.toEntities(images = images),
            snsTag = tag.toEntity(),
        )
}

data class SnsCreateResultDto(
    val snsId: Long,
) {
    companion object {
        operator fun invoke(sns: Sns) = SnsCreateResultDto(snsId = sns.id!!)
    }
}

data class SnsEditDto(
    val id: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<SnsImageDto>,
    val tag: SnsTagDto,
)

data class SnsFindResultDto(
    val id: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val isDisplay: Boolean,
    val images: List<SnsImageDto>,
    val tag: SnsTagDto,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updatedAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(sns: Sns) =
            with(receiver = sns) {
                SnsFindResultDto(
                    id = id!!,
                    title = title,
                    contents = contents,
                    writer = writer,
                    isDisplay = isDisplay,
                    images = snsImages.map { SnsImageDto(url = "https://test/${it.url}", sortOrder = it.sortOrder) },
                    tag = with(receiver = snsTag!!) { SnsTagDto(type = type.name.lowercase(), values = values) },
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
    }
}

data class SnsImageDto(
    val url: String,
    val sortOrder: Int,
) {
    companion object {
        fun toEntities(images: List<SnsImageDto>) =
            SnsImage.createSnsImages(snsImages = images.map { Pair(first = it.url, second = it.sortOrder) })
    }
}

data class SnsTagDto(
    val type: String,
    val values: List<String>,
) {
    fun toEntity() = SnsTag(type = type, values = values)
}

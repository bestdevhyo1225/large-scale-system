package com.hyoseok.service.dto

import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.entity.SnsImage
import com.hyoseok.sns.entity.SnsTag

data class SnsCreateDto(
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<SnsImageDto>,
    val tag: SnsTagCreateDto,
    val productIds: List<Long>,
) {
    fun toEntity() =
        Sns(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            productIds = productIds,
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
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val images: List<SnsImageDto>,
    val tag: SnsTagEditDto,
    val productIds: List<Long>,
) {
    fun toEntity() =
        Sns(
            id = id,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            productIds = productIds,
            snsImages = SnsImageDto.toEntities(images = images),
            snsTag = tag.toEntity(),
        )
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

data class SnsTagCreateDto(
    val type: String,
    val values: List<String>,
) {
    fun toEntity() = SnsTag(type = type, values = values)
}

data class SnsTagEditDto(
    val id: Long,
    val type: String,
    val values: List<String>,
) {
    fun toEntity() = SnsTag(id = id, type = type, values = values)
}

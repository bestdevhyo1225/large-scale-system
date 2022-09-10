package com.hyoseok.sns.entity

import java.time.LocalDateTime
import java.util.Objects

class Sns private constructor(
    id: Long? = null,
    memberId: Long,
    title: String,
    contents: String,
    writer: String,
    isDisplay: Boolean = true,
    productIds: List<Long>,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
    snsImages: List<SnsImage> = listOf(),
    snsTag: SnsTag? = null,
) {

    var id: Long? = id
        private set

    var memberId: Long = memberId
        private set

    var title: String = title
        private set

    var contents: String = contents
        private set

    var writer: String = writer
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var productIds: List<Long> = productIds
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    var snsImages: List<SnsImage> = snsImages
        private set

    var snsTag: SnsTag? = snsTag
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Sns(id=$id, memberId=$memberId, title=$title, contents=$contents, writer=$writer, isDisplay=$isDisplay, " +
            "productIds=$productIds, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSns = (other as? Sns) ?: return false
        return this.id == otherSns.id &&
            this.memberId == otherSns.memberId &&
            this.title == otherSns.title &&
            this.contents == otherSns.contents &&
            this.writer == otherSns.writer &&
            this.isDisplay == otherSns.isDisplay &&
            this.productIds == otherSns.productIds &&
            this.createdAt == otherSns.createdAt &&
            this.updatedAt == otherSns.updatedAt &&
            this.deletedAt == otherSns.deletedAt
    }

    fun changeId(id: Long) {
        this.id = id
    }

    fun toCacheDto() =
        SnsCache(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            isDisplay = isDisplay,
            createdAt = createdAt,
            updatedAt = updatedAt,
            snsImages = snsImages,
            snsTag = snsTag!!,
            products = listOf(),
        )

    companion object {
        operator fun invoke(
            memberId: Long,
            title: String,
            contents: String,
            writer: String,
            productIds: List<Long>,
            snsImages: List<SnsImage>,
            snsTag: SnsTag,
        ) = Sns(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            productIds = productIds,
            createdAt = LocalDateTime.now().withNano(0),
            updatedAt = LocalDateTime.now().withNano(0),
            snsImages = snsImages,
            snsTag = snsTag,
        )

        operator fun invoke(
            id: Long,
            memberId: Long,
            title: String,
            contents: String,
            writer: String,
            isDisplay: Boolean,
            productIds: List<Long>,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
        ) = Sns(
            id = id,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            isDisplay = isDisplay,
            productIds = productIds,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )

        operator fun invoke(
            id: Long,
            memberId: Long,
            title: String,
            contents: String,
            writer: String,
            isDisplay: Boolean,
            productIds: List<Long>,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime,
            deletedAt: LocalDateTime?,
            snsImages: List<SnsImage>,
            snsTag: SnsTag,
        ) = Sns(
            id = id,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            isDisplay = isDisplay,
            productIds = productIds,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            snsImages = snsImages,
            snsTag = snsTag,
        )
    }
}

package com.hyoseok.sns.entity

import java.time.LocalDateTime
import java.util.Objects

class Sns private constructor(
    id: Long? = null,
    title: String,
    contents: String,
    writer: String,
    isDisplay: Boolean = true,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
    snsImages: List<SnsImage>,
    snsTag: SnsTag,
) {

    var id: Long? = id
        private set

    var title: String = title
        private set

    var contents: String = contents
        private set

    var writer: String = writer
        private set

    var isDisplay: Boolean = isDisplay
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    var snsImages: List<SnsImage> = snsImages
        private set

    var snsTag: SnsTag = snsTag
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Sns(id=$id, title=$title, contents=$contents, writer=$writer, isDisplay=$isDisplay, " +
            "createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSns = (other as? Sns) ?: return false
        return this.id == otherSns.id &&
            this.title == otherSns.title &&
            this.contents == otherSns.contents &&
            this.writer == otherSns.writer &&
            this.isDisplay == otherSns.isDisplay &&
            this.createdAt == otherSns.createdAt &&
            this.updatedAt == otherSns.updatedAt &&
            this.deletedAt == otherSns.deletedAt
    }

    companion object {
        operator fun invoke(
            title: String,
            contents: String,
            writer: String,
            snsImages: List<Pair<String, Int>>,
            tagType: String,
            tagValues: List<String>,
        ) = Sns(
            title = title,
            contents = contents,
            writer = writer,
            createdAt = LocalDateTime.now().withNano(0),
            updatedAt = LocalDateTime.now().withNano(0),
            snsImages = SnsImage.createSnsImages(snsImages = snsImages),
            snsTag = SnsTag(type = tagType, values = tagValues),
        )
    }
}

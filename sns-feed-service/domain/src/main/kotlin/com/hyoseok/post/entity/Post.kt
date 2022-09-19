package com.hyoseok.post.entity

import java.time.LocalDateTime
import java.util.Objects

class Post private constructor(
    id: Long? = null,
    memberId: Long,
    title: String,
    contents: String,
    writer: String,
    viewCount: Long = 0,
    images: List<PostImage>,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
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

    var viewCount: Long = viewCount
        private set

    var images: List<PostImage> = images
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    var deletedAt: LocalDateTime? = deletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "Post(id=$id, memberId=$memberId, title=$title, contents=$contents, writer=$writer, viewCount=$viewCount, " +
            "images=$images, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherPost: Post = (other as? Post) ?: return false
        return this.id == otherPost.id &&
            this.memberId == otherPost.memberId &&
            this.title == otherPost.title &&
            this.contents == otherPost.contents &&
            this.writer == otherPost.writer &&
            this.viewCount == otherPost.viewCount &&
            this.images == otherPost.images &&
            this.createdAt == otherPost.createdAt &&
            this.updatedAt == otherPost.updatedAt &&
            this.deletedAt == otherPost.deletedAt
    }

    companion object {
        operator fun invoke(
            memberId: Long,
            title: String,
            contents: String,
            writer: String,
            images: List<PostImage>,
        ) = Post(
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            images = images,
            createdAt = LocalDateTime.now().withNano(0),
            updatedAt = LocalDateTime.now().withNano(0),
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }

    fun toPostCache() =
        PostCache(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            createdAt = createdAt,
            updatedAt = updatedAt,
            images = images,
        )
}

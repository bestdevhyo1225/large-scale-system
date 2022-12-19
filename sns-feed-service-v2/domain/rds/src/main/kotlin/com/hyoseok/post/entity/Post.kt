package com.hyoseok.post.entity

import com.hyoseok.base.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.CascadeType.PERSIST
import javax.persistence.CascadeType.REMOVE
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(
    name = "post",
    indexes = [
        Index(name = "idx_post_01", columnList = "member_id"),
    ],
)
@DynamicUpdate
class Post private constructor(
    memberId: Long,
    title: String,
    contents: String,
    writer: String,
    wishCount: Long = 0,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "contents", nullable = false)
    var contents: String = contents
        protected set

    @Column(name = "writer", nullable = false)
    var writer: String = writer
        protected set

    @Column(name = "wish_count", nullable = false)
    var wishCount: Long = wishCount
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    @OneToMany(mappedBy = "post", cascade = [PERSIST, REMOVE])
    var postImages: MutableList<PostImage> = mutableListOf()

    override fun toString(): String =
        "Post(id=$id, memberId=$memberId, title=$title, contents=$contents, writer=$writer," +
            "wishCount=$wishCount, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    companion object {
        const val POST_IDS_LIMIT_SIZE = 1_000

        operator fun invoke(
            memberId: Long,
            title: String,
            contents: String,
            writer: String,
            postImages: List<PostImage>,
        ): Post =
            Post(
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ).also { post -> postImages.forEach { postImage -> post.addPostImage(postImage = postImage) } }
    }

    fun addPostImage(postImage: PostImage) {
        postImages.add(postImage)
        postImage.changePost(post = this)
    }

    fun updateWishCount(value: Long) {
        if (value > 0) {
            this.wishCount += value
        }
    }
}

package com.hyoseok.post.entity

import com.hyoseok.base.entity.BaseEntity
import com.hyoseok.post.entity.Post.ErrorMessage.INVALID_CURRENT_VIEW_COUNT_IS_ZERO
import com.hyoseok.post.entity.Post.ErrorMessage.INVALID_VIEW_COUNT
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
    viewCount: Long = 0,
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

    @Column(name = "view_count", nullable = false)
    var viewCount: Long = viewCount
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
        "Post(id=$id, memberId=$memberId, title=$title, contents=$contents, writer=$writer, viewCount=$viewCount," +
            "wishCount=$wishCount, createdAt=$createdAt, updatedAt=$updatedAt, deletedAt=$deletedAt)"

    object ErrorMessage {
        const val INVALID_VIEW_COUNT = "유효하지 않는 조회 카운트가 입력되었습니다"
        const val INVALID_CURRENT_VIEW_COUNT_IS_ZERO = "현재 조회 수는 0입니다"
    }

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

    fun increaseViewCount(viewCount: Long) {
        if (viewCount <= 0) {
            throw IllegalArgumentException(INVALID_VIEW_COUNT)
        }
        this.viewCount += viewCount
    }

    fun decreaseViewCount(viewCount: Long) {
        if (viewCount <= 0) {
            throw IllegalArgumentException(INVALID_VIEW_COUNT)
        }
        if (this.viewCount == 0L) {
            throw IllegalArgumentException(INVALID_CURRENT_VIEW_COUNT_IS_ZERO)
        }
        this.viewCount -= viewCount
    }

    fun updateWishCount(value: Long) {
        if (value > 0) {
            this.wishCount += value
        }
    }
}

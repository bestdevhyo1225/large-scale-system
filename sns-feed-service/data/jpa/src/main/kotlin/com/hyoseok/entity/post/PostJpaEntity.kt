package com.hyoseok.entity.post

import com.hyoseok.post.entity.Post
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "post")
@DynamicUpdate
class PostJpaEntity private constructor(
    memberId: Long,
    title: String,
    contents: String,
    writer: String,
    viewCount: Long = 0,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
    deletedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

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

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = deletedAt
        protected set

    @OneToMany(mappedBy = "postJpaEntity", cascade = [CascadeType.PERSIST])
    var postImageJpaEntities: MutableList<PostImageJpaEntity> = mutableListOf()

    fun addPostImageJpaEntity(postImageJpaEntity: PostImageJpaEntity) {
        postImageJpaEntities.add(postImageJpaEntity)
        postImageJpaEntity.changePostJpaEntity(postJpaEntity = this)
    }

    fun mapDomainEntity(post: Post) {
        post.changeId(id = id!!)
        post.images.forEachIndexed { index, postImage ->
            postImage.changeId(id = postImageJpaEntities[index].id!!)
        }
    }

    fun toDomainEntity(isFetchPostImages: Boolean = false) =
        Post(
            id = id!!,
            memberId = memberId,
            title = title,
            contents = contents,
            writer = writer,
            viewCount = viewCount,
            images = if (isFetchPostImages) postImageJpaEntities.map { it.toDomainEntity() } else listOf(),
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
        )

    companion object {
        operator fun invoke(post: Post) =
            with(receiver = post) {
                val postJpaEntity = PostJpaEntity(
                    memberId = memberId,
                    title = title,
                    contents = contents,
                    writer = writer,
                    viewCount = viewCount,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    deletedAt = deletedAt,
                )
                post.images.forEach {
                    postJpaEntity.addPostImageJpaEntity(
                        postImageJpaEntity = PostImageJpaEntity(url = it.url, sortOrder = it.sortOrder),
                    )
                }
                postJpaEntity
            }
    }
}

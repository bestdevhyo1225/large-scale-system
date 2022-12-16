package com.hyoseok.wish.entity

import com.hyoseok.base.entity.BaseEntity
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "wish",
    indexes = [
        Index(name = "uk_wish_01", columnList = "post_id,member_id", unique = true),
    ],
)
@DynamicUpdate
class Wish private constructor(
    postId: Long,
    memberId: Long,
    createdAt: LocalDateTime,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "post_id", nullable = false)
    var postId: Long = postId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    override fun toString() = "Wish(id=$id, postId=$postId, memberId=$memberId, createdAt=$createdAt)"

    companion object {
        operator fun invoke(postId: Long, memberId: Long): Wish =
            Wish(postId = postId, memberId = memberId, createdAt = LocalDateTime.now().withNano(0))
    }
}

package com.hyoseok.follow.entity

import com.hyoseok.base.entity.BaseEntity
import com.hyoseok.follow.entity.Follow.ErrorMessage.SAME_MEMBER_ID
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "follow",
    indexes = [
        Index(name = "uk_follow_01", columnList = "follower_id,followee_id", unique = true),
    ],
)
@DynamicUpdate
class Follow private constructor(
    followerId: Long,
    followeeId: Long,
    createdAt: LocalDateTime,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "follower_id", nullable = false)
    var followerId: Long = followerId
        protected set

    @Column(name = "followee_id", nullable = false)
    var followeeId: Long = followeeId
        protected set

    override fun toString(): String =
        "Follow(id=$id, followerId=$followerId, followeeId=$followeeId, createdAt=$createdAt)"

    object ErrorMessage {
        const val SAME_MEMBER_ID = "동일한 회원입니다"
    }

    companion object {
        const val FIND_MAX_LIMIT = 1_000L

        operator fun invoke(followerId: Long, followeeId: Long): Follow {
            validateFollowerAndFolloweeId(followerId = followerId, followeeId = followeeId)
            return Follow(followerId = followerId, followeeId = followeeId, createdAt = LocalDateTime.now())
        }

        private fun validateFollowerAndFolloweeId(followerId: Long, followeeId: Long) {
            if (followerId == followeeId) {
                throw IllegalArgumentException(SAME_MEMBER_ID)
            }
        }
    }
}

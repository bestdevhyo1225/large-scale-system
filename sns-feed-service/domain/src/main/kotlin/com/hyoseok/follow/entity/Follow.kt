package com.hyoseok.follow.entity

import com.hyoseok.exception.DomainExceptionMessage.FAIL_ADD_FOLLOWEE
import com.hyoseok.exception.DomainExceptionMessage.INVALID_FOLLOWER_ID_FOLLOWEE_ID
import java.util.Objects

class Follow private constructor(
    id: Long? = null,
    followerId: Long,
    followeeId: Long, // followerId 가 follow 하는 followeeId
) {

    var id: Long? = id
        private set

    var followerId: Long = followerId
        private set

    var followeeId: Long = followeeId
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String = "Follow(id=$id, followerId=$followerId, followeeId=$followeeId)"
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherFollow: Follow = (other as? Follow) ?: return false
        return this.id == otherFollow.id &&
            this.followerId == otherFollow.followerId &&
            this.followeeId == otherFollow.followeeId
    }

    companion object {
        const val MAX_FOLLOWEE_LIMIT: Long = 10_000

        fun create(followerId: Long, followeeId: Long, followeeCount: Long): Follow {
            checkFollowerIdAndFolloweeId(followerId = followerId, followeeId = followeeId)
            checkFolloweeCount(value = followeeCount)
            return Follow(followerId = followerId, followeeId = followeeId)
        }

        private fun checkFollowerIdAndFolloweeId(followerId: Long, followeeId: Long) {
            if (followerId == followeeId) {
                throw IllegalArgumentException(INVALID_FOLLOWER_ID_FOLLOWEE_ID)
            }
        }

        private fun checkFolloweeCount(value: Long) {
            if (value > MAX_FOLLOWEE_LIMIT) {
                throw IllegalArgumentException(FAIL_ADD_FOLLOWEE)
            }
        }

        operator fun invoke(followerId: Long, followeeId: Long) =
            Follow(followerId = followerId, followeeId = followeeId)

        operator fun invoke(id: Long, followerId: Long, followeeId: Long) =
            Follow(id = id, followerId = followerId, followeeId = followeeId)
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

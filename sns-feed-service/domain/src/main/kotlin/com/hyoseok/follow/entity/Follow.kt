package com.hyoseok.follow.entity

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
        operator fun invoke(followerId: Long, followeeId: Long) =
            Follow(followerId = followerId, followeeId = followeeId)
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

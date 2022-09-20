package com.hyoseok.entity.follow

import com.hyoseok.follow.entity.Follow
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "follow")
@DynamicUpdate
class FollowJpaEntity private constructor(
    followerId: Long,
    followeeId: Long,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "follower_id", nullable = false)
    var followerId: Long = followerId
        protected set

    @Column(name = "followee_id", nullable = false)
    var followeeId: Long = followeeId
        protected set

    companion object {
        operator fun invoke(follow: Follow) =
            with(receiver = follow) {
                FollowJpaEntity(followerId = followerId, followeeId = followeeId)
            }
    }

    fun mapDomainEntity(follow: Follow) {
        follow.changeId(id = id!!)
    }
}

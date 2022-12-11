package com.hyoseok.follow.entity

import com.hyoseok.base.entity.BaseEntity
import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_TOTAL_FOLLOWEE
import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_TOTAL_FOLLOWER
import com.hyoseok.follow.entity.FollowCount.ErrorMessage.INVALID_VALUE
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "follow_count",
    indexes = [
        Index(name = "idx_follow_count_01", columnList = "member_id"),
    ],
)
@DynamicUpdate
class FollowCount(
    memberId: Long,
    totalFollower: Long,
    totalFollowee: Long,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) : BaseEntity(createdAt = createdAt) {

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "total_follower", nullable = false)
    var totalFollower: Long = totalFollower
        protected set

    @Column(name = "total_followee", nullable = false)
    var totalFollowee: Long = totalFollowee
        protected set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    var updatedAt: LocalDateTime = updatedAt
        protected set

    override fun toString(): String =
        "FollowCount(id=$id, memberId=$memberId, total=$totalFollower, totalFollowee=$totalFollowee," +
            "createdAt=$createdAt, updatedAt=$updatedAt)"

    object ErrorMessage {
        const val INVALID_VALUE = "유효하지 않는 값입니다."
        const val INVALID_TOTAL_FOLLOWER = "totalFollower 값이 유효하지 않습니다."
        const val INVALID_TOTAL_FOLLOWEE = "totalFollowee 값이 유효하지 않습니다."
    }

    companion object {
        const val INFLUENCER_CHECK_TOTAL_COUNT = 10_000L

        operator fun invoke(memberId: Long, totalFollower: Long, totalFollowee: Long): FollowCount {
            val nowDateTime: LocalDateTime = LocalDateTime.now().withNano(0)
            return FollowCount(
                memberId = memberId,
                totalFollower = totalFollower,
                totalFollowee = totalFollowee,
                createdAt = nowDateTime,
                updatedAt = nowDateTime,
            )
        }
    }

    fun increaseTotalFollowerOne() {
        increaseTotalFollower(value = 1L)
    }

    fun increaseTotalFollower(value: Long) {
        if (value < 0) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
        this.totalFollower += value
    }

    fun decreaseTotalFollower(value: Long) {
        if (value < 0) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
        if (this.totalFollower - value < 0) {
            throw IllegalArgumentException(INVALID_TOTAL_FOLLOWER)
        }
        this.totalFollower -= value
    }

    fun increaseTotalFolloweeOne() {
        increaseTotalFollowee(value = 1L)
    }

    fun increaseTotalFollowee(value: Long) {
        if (value < 0) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
        this.totalFollowee += value
    }

    fun decreaseTotalFollowee(value: Long) {
        if (value < 0) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
        if (this.totalFollowee - value < 0) {
            throw IllegalArgumentException(INVALID_TOTAL_FOLLOWEE)
        }
        this.totalFollowee -= value
    }
}

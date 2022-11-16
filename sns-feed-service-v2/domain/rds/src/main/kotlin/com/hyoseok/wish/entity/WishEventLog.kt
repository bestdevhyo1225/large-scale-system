package com.hyoseok.wish.entity

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wish_event_log")
@DynamicUpdate
class WishEventLog private constructor(
    postId: Long,
    memberId: Long,
    publishedAt: LocalDateTime,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "post_id", nullable = false)
    var postId: Long = postId
        protected set

    @Column(name = "member_id", nullable = false)
    var memberId: Long = memberId
        protected set

    @Column(name = "is_processed", nullable = false)
    var isProcessed: Boolean = false
        protected set

    @Column(name = "published_at", nullable = false, columnDefinition = "DATETIME")
    var publishedAt: LocalDateTime = publishedAt
        protected set

    @Column(name = "processed_at", columnDefinition = "DATETIME")
    var processedAt: LocalDateTime? = null
        protected set

    override fun toString() = "WishEventLog(id=$id, postId=$postId, memberId=$memberId, isProcessed=$isProcessed, " +
        "publishedAt=$publishedAt, processedAt=$processedAt)"

    companion object {
        operator fun invoke(postId: Long, memberId: Long): WishEventLog {
            val nowDateTime: LocalDateTime = LocalDateTime.now().withNano(0)
            return WishEventLog(postId = postId, memberId = memberId, publishedAt = nowDateTime)
        }
    }

    fun completeWishEventProcessing() {
        this.isProcessed = true
        this.processedAt = LocalDateTime.now().withNano(0)
    }
}

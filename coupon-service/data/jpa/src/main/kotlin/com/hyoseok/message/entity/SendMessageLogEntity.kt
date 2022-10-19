package com.hyoseok.message.entity

import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "send_message_log")
@DynamicUpdate
class SendMessageLogEntity private constructor(
    instanceId: String,
    data: String,
    isSendCompleted: Boolean,
    createdAt: LocalDateTime,
    sendCompletedAt: LocalDateTime?,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(name = "instance_id", nullable = false)
    var instanceId: String = instanceId
        protected set

    @Column(nullable = false)
    var data: String = data
        protected set

    @Column(name = "is_send_completed", nullable = false)
    var isSendCompleted: Boolean = isSendCompleted
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    @Column(name = "send_completed_at", columnDefinition = "DATETIME")
    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        protected set

    companion object {
        operator fun invoke(sendMessageLog: SendMessageLog) =
            with(receiver = sendMessageLog) {
                SendMessageLogEntity(
                    instanceId = instanceId,
                    data = data,
                    isSendCompleted = isSendCompleted,
                    createdAt = createdAt,
                    sendCompletedAt = sendCompletedAt,
                )
            }
    }

    fun toDomain() =
        SendMessageLog(
            id = id!!,
            instanceId = instanceId,
            data = data,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
            sendCompletedAt = sendCompletedAt,
        )
}

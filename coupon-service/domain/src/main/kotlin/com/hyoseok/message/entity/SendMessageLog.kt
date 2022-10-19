package com.hyoseok.message.entity

import java.time.LocalDateTime
import java.util.Objects

class SendMessageLog private constructor(
    id: Long = 0,
    instanceId: String,
    data: String,
    isSendCompleted: Boolean = false,
    createdAt: LocalDateTime,
    sendCompletedAt: LocalDateTime? = null,
) {

    var id: Long = id
        private set

    var instanceId: String = instanceId
        private set

    var data: String = data
        private set

    var isSendCompleted: Boolean = isSendCompleted
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "SendMessageLog(id=$id, instanceId=$instanceId, data=$data, isSendCompleted=$isSendCompleted, " +
            "createdAt=$createdAt, sendCompletedAt=$sendCompletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSendMessageLog: SendMessageLog = (other as? SendMessageLog) ?: return false
        return this.id == otherSendMessageLog.id &&
            this.instanceId == otherSendMessageLog.instanceId &&
            this.data == otherSendMessageLog.data &&
            this.isSendCompleted == otherSendMessageLog.isSendCompleted &&
            this.createdAt == otherSendMessageLog.createdAt &&
            this.sendCompletedAt == otherSendMessageLog.sendCompletedAt
    }

    companion object {
        operator fun invoke(instanceId: String, data: String) =
            SendMessageLog(
                instanceId = instanceId,
                data = data,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            id: Long,
            instanceId: String,
            data: String,
            isSendCompleted: Boolean,
            createdAt: LocalDateTime,
            sendCompletedAt: LocalDateTime?,
        ) = SendMessageLog(
            id = id,
            instanceId = instanceId,
            data = data,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
            sendCompletedAt = sendCompletedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

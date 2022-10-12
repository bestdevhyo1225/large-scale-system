package com.hyoseok.message.entity

import java.time.LocalDateTime
import java.util.Objects

class SendMessageFailLog private constructor(
    id: Long = 0,
    instanceId: String,
    data: String,
    errorMessage: String,
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

    var errorMessage: String = errorMessage
        private set

    var isSendCompleted: Boolean = isSendCompleted
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    var sendCompletedAt: LocalDateTime? = sendCompletedAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "SendMessageFailLog(id=$id, instanceId=$instanceId, data=$data, errorMessage=$errorMessage, " +
            "isSendCompleted=$isSendCompleted, createdAt=$createdAt, sendCompletedAt=$sendCompletedAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherSendMessageFailLog: SendMessageFailLog = (other as? SendMessageFailLog) ?: return false
        return this.id == otherSendMessageFailLog.id &&
            this.instanceId == otherSendMessageFailLog.instanceId &&
            this.data == otherSendMessageFailLog.data &&
            this.errorMessage == otherSendMessageFailLog.errorMessage &&
            this.isSendCompleted == otherSendMessageFailLog.isSendCompleted &&
            this.createdAt == otherSendMessageFailLog.createdAt &&
            this.sendCompletedAt == otherSendMessageFailLog.sendCompletedAt
    }

    companion object {
        operator fun invoke(instanceId: String, data: String, errorMessage: String) =
            SendMessageFailLog(
                instanceId = instanceId,
                data = data,
                errorMessage = errorMessage,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            id: Long,
            instanceId: String,
            data: String,
            errorMessage: String,
            isSendCompleted: Boolean,
            createdAt: LocalDateTime,
            sendCompletedAt: LocalDateTime?,
        ) = SendMessageFailLog(
            id = id,
            instanceId = instanceId,
            data = data,
            errorMessage = errorMessage,
            isSendCompleted = isSendCompleted,
            createdAt = createdAt,
            sendCompletedAt = sendCompletedAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

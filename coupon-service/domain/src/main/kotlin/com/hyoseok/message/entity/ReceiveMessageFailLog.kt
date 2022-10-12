package com.hyoseok.message.entity

import java.time.LocalDateTime
import java.util.Objects

class ReceiveMessageFailLog private constructor(
    id: Long = 0,
    instanceId: String,
    data: String,
    errorMessage: String,
    useRetry: Boolean,
    createdAt: LocalDateTime,
) {

    var id: Long = id
        private set

    var instanceId: String = instanceId
        private set

    var data: String = data
        private set

    var errorMessage: String = errorMessage
        private set

    var useRetry: Boolean = useRetry
        private set

    var createdAt: LocalDateTime = createdAt
        private set

    override fun hashCode(): Int = Objects.hash(id)
    override fun toString(): String =
        "ReceiveMessageFailLog(id=$id, instanceId=$instanceId, data=$data, errorMessage=$errorMessage, " +
            "useRetry=$useRetry, createdAt=$createdAt)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherReceiveMessageFailLog: ReceiveMessageFailLog = (other as? ReceiveMessageFailLog) ?: return false
        return this.id == otherReceiveMessageFailLog.id &&
            this.instanceId == otherReceiveMessageFailLog.instanceId &&
            this.data == otherReceiveMessageFailLog.data &&
            this.errorMessage == otherReceiveMessageFailLog.errorMessage &&
            this.useRetry == otherReceiveMessageFailLog.useRetry &&
            this.createdAt == otherReceiveMessageFailLog.createdAt
    }

    companion object {
        operator fun invoke(instanceId: String, data: String, errorMessage: String, useRetry: Boolean) =
            ReceiveMessageFailLog(
                instanceId = instanceId,
                data = data,
                errorMessage = errorMessage,
                useRetry = useRetry,
                createdAt = LocalDateTime.now().withNano(0),
            )

        operator fun invoke(
            id: Long,
            instanceId: String,
            data: String,
            errorMessage: String,
            useRetry: Boolean,
            createdAt: LocalDateTime,
        ) = ReceiveMessageFailLog(
            id = id,
            instanceId = instanceId,
            data = data,
            errorMessage = errorMessage,
            useRetry = useRetry,
            createdAt = createdAt,
        )
    }

    fun changeId(id: Long) {
        this.id = id
    }
}

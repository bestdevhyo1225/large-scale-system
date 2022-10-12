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
@Table(name = "receive_message_fail_log")
@DynamicUpdate
class ReceiveMessageFailLogEntity private constructor(
    instanceId: String,
    data: String,
    errorMessage: String,
    useRetry: Boolean,
    createdAt: LocalDateTime,
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

    @Column(name = "error_message", length = 2_500, nullable = false)
    var errorMessage: String = errorMessage
        protected set

    @Column(name = "is_send_completed", nullable = false)
    var useRetry: Boolean = useRetry
        protected set

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    var createdAt: LocalDateTime = createdAt
        protected set

    companion object {
        operator fun invoke(receiveMessageFailLog: ReceiveMessageFailLog) =
            with(receiver = receiveMessageFailLog) {
                ReceiveMessageFailLogEntity(
                    instanceId = instanceId,
                    data = data,
                    errorMessage = errorMessage,
                    useRetry = useRetry,
                    createdAt = createdAt,
                )
            }
    }

    fun toDomain() =
        ReceiveMessageFailLog(
            id = id!!,
            instanceId = instanceId,
            data = data,
            errorMessage = errorMessage,
            useRetry = useRetry,
            createdAt = createdAt,
        )
}

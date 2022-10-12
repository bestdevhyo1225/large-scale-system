package com.hyoseok.consumer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.message.entity.ReceiveMessageFailLog
import com.hyoseok.message.repository.ReceiveMessageFailLogRepository
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class KafkaCouponIssuedErrorHandler(
    private val receiveMessageFailLogRepository: ReceiveMessageFailLogRepository,
) : KafkaListenerErrorHandler {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    // 해당 메서드는 호출되지 않음
    override fun handleError(message: Message<*>, exception: ListenerExecutionFailedException): Any {
        return message
    }

    override fun handleError(
        message: Message<*>,
        exception: ListenerExecutionFailedException,
        consumer: Consumer<*, *>,
    ): Any {
        if (exception.cause is DataIntegrityViolationException) {
            consumer.commitSync()
            logger.error { "DataIntegrityViolationException has occured, and commitSync() is called" }
        } else {
            logger.error { exception.cause }
        }

        val instanceId: String = consumer.groupMetadata()
            .groupInstanceId()
            .orElseGet { "consumer-default-${UUID.randomUUID()}" }
        val consumerRecord: ConsumerRecord<*, *> = message.payload as ConsumerRecord<*, *>

        receiveMessageFailLogRepository.save(
            receiveMessageFailLog = ReceiveMessageFailLog(
                instanceId = instanceId,
                data = jacksonObjectMapper.writeValueAsString(consumerRecord.value()),
                errorMessage = exception.cause?.localizedMessage ?: exception.localizedMessage,
                useRetry = false,
            ),
        )

        return message // @SendTo 설정한 토픽으로 message 가 전송된다. (재시도 처리)
    }
}

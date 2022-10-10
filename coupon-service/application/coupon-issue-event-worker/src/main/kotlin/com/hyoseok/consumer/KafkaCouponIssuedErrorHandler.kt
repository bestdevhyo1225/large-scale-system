package com.hyoseok.consumer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.entity.CouponIssuedFailLog
import com.hyoseok.coupon.entity.enum.CouponIssuedFailLogApplicationType
import com.hyoseok.coupon.repository.CouponIssuedFailLogRepository
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Component
class KafkaCouponIssuedErrorHandler(
    private val couponIssuedFailLogRepository: CouponIssuedFailLogRepository,
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

        val consumerRecord: ConsumerRecord<*, *> = message.payload as ConsumerRecord<*, *>

        couponIssuedFailLogRepository.save(
            couponIssuedFailLog = CouponIssuedFailLog(
                applicationType = CouponIssuedFailLogApplicationType.CONSUMER,
                data = jacksonObjectMapper.writeValueAsString(consumerRecord.value()),
                errorMessage = exception.cause?.localizedMessage ?: exception.localizedMessage,
            ),
        )

        return message // @SendTo 설정한 토픽으로 message 가 전송된다. (재시도 처리)
    }
}

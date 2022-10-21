package com.hyoseok.coupon.publisher

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.publisher.dto.CouponIssuedSendCompletedDto
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
@ConditionalOnProperty(prefix = "infrastructure.enable.kafka.producer", name = ["coupon"], havingValue = "true")
class KafkaCouponProducerCallback(
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ListenableFutureCallback<SendResult<String, String>> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun onSuccess(result: SendResult<String, String>?) {
        if (result == null) {
            return logger.error { "SendResult is NULL" }
        }

        logger.info { "partition: ${result.recordMetadata.partition()}, offset: ${result.recordMetadata.offset()}" }

        applicationEventPublisher.publishEvent(
            jacksonObjectMapper.readValue(
                result.producerRecord.value(),
                CouponIssuedSendCompletedDto::class.java,
            ),
        )
    }

    override fun onFailure(ex: Throwable) {
        logger.error { ex }
    }
}

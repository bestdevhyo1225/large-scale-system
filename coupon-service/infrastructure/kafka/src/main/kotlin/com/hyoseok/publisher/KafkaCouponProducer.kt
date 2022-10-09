package com.hyoseok.publisher

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.coupon.service.CouponMessageBrokerProducer
import com.hyoseok.coupon.exception.CouponProducerSendFailedException
import mu.KotlinLogging
import org.apache.kafka.common.KafkaException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
@ConditionalOnProperty(prefix = "infrastructure.enable.kafka.producer", name = ["coupon"], havingValue = "true")
class KafkaCouponProducer(
    @Value("\${infrastructure.kafka.topics.coupon-issued}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : CouponMessageBrokerProducer {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    override fun <T : Any> send(event: T) {
        execute {
            val recordMetadata = kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .get()
                .recordMetadata

            logger.info { "partition: ${recordMetadata.partition()}, offset: ${recordMetadata.offset()}" }
        }
    }

    override fun <T : Any> sendAsync(event: T) {
        execute {
            kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .addCallback(KafkaCouponProducerCallback())
        }
    }

    private fun execute(func: () -> Unit) {
        try {
            func()
        } catch (exception: InterruptedException) {
            throw CouponProducerSendFailedException(message = exception.localizedMessage)
        } catch (exception: ExecutionException) {
            throw CouponProducerSendFailedException(message = exception.localizedMessage)
        } catch (exception: KafkaException) {
            throw CouponProducerSendFailedException(message = exception.localizedMessage)
        }
    }
}

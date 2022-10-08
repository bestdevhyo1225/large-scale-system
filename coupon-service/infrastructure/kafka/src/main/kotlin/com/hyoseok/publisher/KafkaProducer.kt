package com.hyoseok.publisher

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.exception.KafkaProducerSendFailedException
import com.hyoseok.service.MessageBrokerProducer
import mu.KotlinLogging
import org.apache.kafka.common.KafkaException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
@ConditionalOnProperty(prefix = "infrastructure.enable", name = ["kafka"], havingValue = "true")
class KafkaProducer(
    @Value("\${infrastructure.kafka.topic.coupon-issued}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) : MessageBrokerProducer {

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
                .addCallback(KafkaProducerCallback())
        }
    }

    private fun execute(func: () -> Unit) {
        try {
            func()
        } catch (exception: InterruptedException) {
            throw KafkaProducerSendFailedException(message = exception.localizedMessage)
        } catch (exception: ExecutionException) {
            throw KafkaProducerSendFailedException(message = exception.localizedMessage)
        } catch (exception: KafkaException) {
            throw KafkaProducerSendFailedException(message = exception.localizedMessage)
        }
    }
}

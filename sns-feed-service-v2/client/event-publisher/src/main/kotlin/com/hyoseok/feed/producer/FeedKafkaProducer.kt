package com.hyoseok.feed.producer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.config.KafkaProducerCallback
import mu.KotlinLogging
import org.apache.kafka.common.KafkaException
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
class FeedKafkaProducer(
    @Value("\${spring.kafka.topics.feed}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val kafkaProducerCallback: KafkaProducerCallback,
) {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun <T : Any> send(event: T) {
        execute {
            val recordMetadata = kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .get()
                .recordMetadata

            logger.info { "partition: ${recordMetadata.partition()}, offset: ${recordMetadata.offset()}" }
        }
    }

    fun <T : Any> sendAsync(event: T) {
        execute {
            kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .addCallback(kafkaProducerCallback)
        }
    }

    private fun execute(func: () -> Unit) {
        try {
            func()
        } catch (exception: InterruptedException) {
            logger.error { exception }
        } catch (exception: ExecutionException) {
            logger.error { exception }
        } catch (exception: KafkaException) {
            logger.error { exception }
        }
    }
}

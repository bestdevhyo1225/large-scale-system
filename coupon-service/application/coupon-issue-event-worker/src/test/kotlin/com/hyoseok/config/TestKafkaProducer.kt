package com.hyoseok.config

import mu.KotlinLogging
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestComponent
import org.springframework.kafka.core.KafkaTemplate

@TestComponent
class TestKafkaProducer(
    @Value("\${spring.kafka.topics.coupon-issued}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    private val logger = KotlinLogging.logger {}

    fun send(payload: String) {
        logger.info { "topic: $topic, payload: $payload" }

        val recordMetadata: RecordMetadata = kafkaTemplate
            .send(topic, payload)
            .get()
            .recordMetadata

        logger.info { "partition: ${recordMetadata.partition()}, offset: ${recordMetadata.offset()}" }
    }
}

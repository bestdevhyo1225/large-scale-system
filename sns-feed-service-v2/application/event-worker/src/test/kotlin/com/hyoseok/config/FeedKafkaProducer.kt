package com.hyoseok.config

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestComponent
import org.springframework.kafka.core.KafkaTemplate

@TestComponent
class FeedKafkaProducer(
    @Value("\${spring.kafka.topics.feed}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    private val logger = KotlinLogging.logger {}

    fun send(payload: String) {
        logger.info { "topic: $topic, payload: $payload" }

        kafkaTemplate
            .send(topic, payload)
            .addCallback(KafkaProducerCallback())
    }
}

package com.hyoseok.wish.producer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.common.AbstractKafkaProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class WishKafkaProducer(
    @Value("\${spring.kafka.topics.wish}")
    private val topic: String,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val wishKafkaProducerCallback: WishKafkaProducerCallback,
) : AbstractKafkaProducer() {

    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun <T : Any> sendAsync(event: T) {
        execute {
            kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .addCallback(wishKafkaProducerCallback)
        }
    }
}

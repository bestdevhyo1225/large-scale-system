package com.hyoseok.publisher

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.apache.kafka.common.KafkaException
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutionException

@Component
@ConditionalOnProperty(prefix = "infrastructure.enable", name = ["kafka"], havingValue = "true")
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    fun <T : Any> send(event: T, topic: String) {
        execute {
            val recordMetadata = kafkaTemplate
                .send(topic, jacksonObjectMapper.writeValueAsString(event))
                .get()
                .recordMetadata

            logger.info { "partition: ${recordMetadata.partition()}, offset: ${recordMetadata.offset()}" }
        }
    }

    private fun execute(func: () -> Unit) {
        try {
            func()
        } catch (exception: InterruptedException) {
            throw RuntimeException(exception.localizedMessage)
        } catch (exception: ExecutionException) {
            throw RuntimeException(exception.localizedMessage)
        } catch (exception: KafkaException) {
            throw RuntimeException(exception.localizedMessage)
        }
    }
}

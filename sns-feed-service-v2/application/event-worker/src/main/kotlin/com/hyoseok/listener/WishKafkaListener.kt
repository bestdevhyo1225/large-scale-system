package com.hyoseok.listener

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.listener.ListenerErrorMessage.ACKNOWLEDGMENT_IS_NULL
import com.hyoseok.listener.dto.WishEventDto
import com.hyoseok.wish.service.WishService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class WishKafkaListener(
    private val wishService: WishService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        topics = ["\${spring.kafka.topics.wish}"],
        containerFactory = "wishKafkaListenerContainerFactory",
        errorHandler = "wishKafkaListenerErrorHandler",
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        logger.info { "partition: ${data.partition()}, offset: ${data.offset()}" }

        jacksonObjectMapper.readValue(data.value(), WishEventDto::class.java)
            .also { wishService.create(postId = it.postId, memberId = it.memberId) }

        acknowledgment?.acknowledge() ?: throw RuntimeException(ACKNOWLEDGMENT_IS_NULL)

        logger.info { "completed acknowledge()" }
    }
}

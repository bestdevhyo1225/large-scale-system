package com.hyoseok.listener

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.feed.service.FeedRedisService
import com.hyoseok.listener.ListenerErrorMessage.ACKNOWLEDGMENT_IS_NULL
import com.hyoseok.listener.dto.FeedEventDto
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class FeedKafkaListener(
    private val feedRedisService: FeedRedisService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        topics = ["\${spring.kafka.topics.feed}"],
        containerFactory = "feedKafkaListenerContainerFactory",
        errorHandler = "feedKafkaListenerErrorHandler",
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        logger.info { "partition: ${data.partition()}, offset: ${data.offset()}" }

        jacksonObjectMapper.readValue(data.value(), FeedEventDto::class.java)
            .also { feedRedisService.create(memberId = it.followerId, postId = it.postId) }

        acknowledgment?.acknowledge() ?: throw RuntimeException(ACKNOWLEDGMENT_IS_NULL)

        logger.info { "completed acknowledge()" }

        // 메시지 수신 완료 로그 업데이트?
    }
}

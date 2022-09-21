package com.hyoseok.listener

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hyoseok.config.KafkaConsumerConcurrency.SNS_FEED_TOPIC_PARTITION_COUNT
import com.hyoseok.config.KafkaConsumerGroups.SNS_FEED_WORKER
import com.hyoseok.config.KafkaTopics.SNS_FEED
import com.hyoseok.listener.dto.FollowerSendEventDto
import com.hyoseok.service.FeedService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class KafkaSnsFeedConsumer(
    private val feedService: FeedService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        topics = [SNS_FEED],
        groupId = SNS_FEED_WORKER,
        concurrency = SNS_FEED_TOPIC_PARTITION_COUNT,
        containerFactory = "kafkaListenerContainerFactory",
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        logger.info { "partition: ${data.partition()}, offset: ${data.offset()}" }

        val eventDto: FollowerSendEventDto = jacksonObjectMapper.readValue(data.value())

        with(receiver = eventDto) {
            feedService.create(postId = postId, memberId = memberId, createdAt = createdAt)
        }

        acknowledgment?.acknowledge()
    }
}

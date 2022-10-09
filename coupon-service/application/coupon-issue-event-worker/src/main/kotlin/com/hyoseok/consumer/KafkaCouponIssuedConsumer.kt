package com.hyoseok.consumer

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyoseok.consumer.dto.CouponIssuedCreateDto
import com.hyoseok.exception.CouponIssuedEventWorkerMessage.ACKNOWLEDGMENT_IS_NULL
import com.hyoseok.service.CouponIssuedService
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class KafkaCouponIssuedConsumer(
    private val couponIssuedService: CouponIssuedService,
) : AcknowledgingMessageListener<String, String> {

    private val logger = KotlinLogging.logger {}
    private val jacksonObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @KafkaListener(
        containerFactory = "kafkaListenerContainerFactory",
        topics = ["\${spring.kafka.topics.coupon-issued}"],
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        logger.info { "partition: ${data.partition()}, offset: ${data.offset()}" }

        with(receiver = jacksonObjectMapper.readValue(data.value(), CouponIssuedCreateDto::class.java)) {
            couponIssuedService.create(couponId = couponId, memberId = memberId)
        }

        acknowledgment?.acknowledge() ?: throw RuntimeException(ACKNOWLEDGMENT_IS_NULL)
    }
}

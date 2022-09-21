package com.hyoseok.config

import org.apache.kafka.clients.consumer.ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.RoundRobinAssignor
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties.AckMode

@Configuration
@EnableKafka
class KafkaConsumerConfig(
    @Value("\${spring.kafka.consumer.allow-auto-create-topics}")
    private val allowAutoCreateTopics: Boolean,

    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${spring.kafka.consumer.auto-offset-reset}")
    private val autoOffsetReset: String,

    @Value("\${spring.kafka.consumer.session-timeout}")
    private val sessionTimeout: Int,

    @Value("\${spring.kafka.consumer.heartbeat-interval}")
    private val heartbeatInterval: Int,

    @Value("\${spring.kafka.consumer.max-poll-interval}")
    private val maxPollInterval: Int,

    @Value("\${spring.kafka.consumer.max-poll-records}")
    private val maxPollRecords: Int,
) {

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val props = mutableMapOf<String, Any>()

        props[ALLOW_AUTO_CREATE_TOPICS_CONFIG] = allowAutoCreateTopics
        props[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ENABLE_AUTO_COMMIT_CONFIG] = false // 수동 커밋
        props[AUTO_OFFSET_RESET_CONFIG] = autoOffsetReset
        props[SESSION_TIMEOUT_MS_CONFIG] = sessionTimeout
        props[HEARTBEAT_INTERVAL_MS_CONFIG] = heartbeatInterval
        props[MAX_POLL_INTERVAL_MS_CONFIG] = maxPollInterval
        props[MAX_POLL_RECORDS_CONFIG] = maxPollRecords
        props[PARTITION_ASSIGNMENT_STRATEGY_CONFIG] = listOf(RoundRobinAssignor::class.java) // 라운드 로빈 방식으로 할당
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(props)
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()

        containerFactory.containerProperties.ackMode = AckMode.MANUAL_IMMEDIATE // 즉시 수동 커밋
        containerFactory.consumerFactory = consumerFactory

        return containerFactory
    }
}

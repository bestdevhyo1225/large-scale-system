package com.hyoseok.config

import org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.PARTITIONER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.UniformStickyPartitioner
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@EnableKafka
@ConditionalOnProperty(prefix = "infrastructure.enable", name = ["kafka"], havingValue = "true")
class KafkaProducerConfig(
    @Value("\${infrastructure.kafka.producer.bootstrap-servers}")
    private val bootstrapServers: String,

    @Value("\${infrastructure.kafka.producer.acks}")
    private val acks: String,

    @Value("\${infrastructure.kafka.producer.buffer-memory}")
    private val bufferMemory: Long,

    @Value("\${infrastructure.kafka.producer.compression-type}")
    private val compressionType: String,

    @Value("\${infrastructure.kafka.producer.retries}")
    private val retries: Int,

    @Value("\${infrastructure.kafka.producer.batch-size}")
    private val batchSize: Int,

    @Value("\${infrastructure.kafka.producer.linger-ms}")
    private val lingerMs: Int,

    @Value("\${infrastructure.kafka.producer.request-timeout-ms}")
    private val requestTimeoutMs: Int,

    @Value("\${infrastructure.kafka.producer.delivery-timeout-ms}")
    private val deliveryTimeoutMs: Int,
) {

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        val props = mutableMapOf<String, Any>()

        props[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ACKS_CONFIG] = acks
        props[BUFFER_MEMORY_CONFIG] = bufferMemory
        props[COMPRESSION_TYPE_CONFIG] = compressionType
        props[RETRIES_CONFIG] = retries
        props[BATCH_SIZE_CONFIG] = batchSize
        props[LINGER_MS_CONFIG] = lingerMs
        props[REQUEST_TIMEOUT_MS_CONFIG] = requestTimeoutMs
        props[DELIVERY_TIMEOUT_MS_CONFIG] = deliveryTimeoutMs
        // 카프카로 전송하는 메시지의 순서가 그다지 중요하지 않다면, 스티키 파티셔닝 전략을 적용하기를 권장한다.
        // 피드 발행의 경우, 메시지 순서가 중요하지 않다.
        // 파티션을 2개 이상으로 나눈 경우에 효과가 있다.
        props[PARTITIONER_CLASS_CONFIG] = UniformStickyPartitioner::class.java
        props[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return KafkaTemplate(DefaultKafkaProducerFactory(props))
    }
}

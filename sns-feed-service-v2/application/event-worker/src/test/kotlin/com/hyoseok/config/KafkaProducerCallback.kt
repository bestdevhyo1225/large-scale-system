package com.hyoseok.config

import mu.KotlinLogging
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback

class KafkaProducerCallback : ListenableFutureCallback<SendResult<String, String>> {

    private val logger = KotlinLogging.logger {}

    override fun onSuccess(result: SendResult<String, String>?) {
        if (result == null) {
            return logger.error { "SendResult is NULL" }
        }

        logger.info { "partition: ${result.recordMetadata.partition()}, offset: ${result.recordMetadata.offset()}" }
    }

    override fun onFailure(ex: Throwable) {
        logger.error { ex }
    }
}

package com.hyoseok.coupon.publisher

import mu.KotlinLogging
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

@Component
class KafkaCouponProducerCallback : ListenableFutureCallback<SendResult<String, String>> {

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
package com.bestdev.service.payment

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PaymentExternalService {

    private val logger = KotlinLogging.logger {}

    fun request(merchantUniqueId: Long) {
        logger.info { "merchantUniqueId: $merchantUniqueId" }

        Thread.sleep(3_000)
    }
}

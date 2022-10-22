package com.hyoseok

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class CouponBatchApplication

fun main(args: Array<String>) {
    runApplication<CouponBatchApplication>(*args)
}

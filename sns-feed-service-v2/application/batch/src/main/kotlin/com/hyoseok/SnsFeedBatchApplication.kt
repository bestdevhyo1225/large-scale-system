package com.hyoseok

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class SnsFeedBatchApplication

fun main(args: Array<String>) {
    runApplication<SnsFeedBatchApplication>(*args)
}

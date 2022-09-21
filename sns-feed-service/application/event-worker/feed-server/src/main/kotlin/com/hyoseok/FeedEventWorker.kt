package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeedEventWorker

fun main(args: Array<String>) {
    runApplication<FeedEventWorker>(*args)
}

package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnsFeedEventWorker

fun main(args: Array<String>) {
    runApplication<SnsFeedEventWorker>(*args)
}

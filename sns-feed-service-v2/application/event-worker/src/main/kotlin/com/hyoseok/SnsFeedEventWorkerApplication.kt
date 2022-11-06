package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnsFeedEventWorkerApplication

fun main(args: Array<String>) {
    runApplication<SnsFeedEventWorkerApplication>(*args)
}

package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortenedUrlServerApplication

fun main(args: Array<String>) {
    runApplication<ShortenedUrlServerApplication>(*args)
}

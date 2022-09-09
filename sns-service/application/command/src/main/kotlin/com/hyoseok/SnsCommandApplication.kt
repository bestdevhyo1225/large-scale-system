package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SnsCommandApplication

fun main(args: Array<String>) {
    runApplication<SnsCommandApplication>(*args)
}

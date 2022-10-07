package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CouponCommandApiApplication

fun main(args: Array<String>) {
    runApplication<CouponCommandApiApplication>(*args)
}

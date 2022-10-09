package com.hyoseok

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CouponIssuedEventWorker

fun main(args: Array<String>) {
    runApplication<CouponIssuedEventWorker>(*args)
}

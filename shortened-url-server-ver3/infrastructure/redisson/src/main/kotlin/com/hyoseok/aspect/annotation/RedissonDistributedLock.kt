package com.hyoseok.aspect.annotation

import java.util.concurrent.TimeUnit

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedissonDistributedLock(
    val waitTime: Long,
    val leaseTime: Long,
    val timeUnit: TimeUnit,
)

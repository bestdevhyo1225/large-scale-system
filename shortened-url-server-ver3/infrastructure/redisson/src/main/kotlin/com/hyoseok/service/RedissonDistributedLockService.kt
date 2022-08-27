package com.hyoseok.service

import com.hyoseok.aspect.annotation.RedissonDistributedLock
import com.hyoseok.config.RedissonUseType
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedissonDistributedLockService {

    @RedissonDistributedLock(waitTime = 3_000, leaseTime = 3_000, timeUnit = TimeUnit.MILLISECONDS)
    fun <V : Any, R : Any> executeWithLock(value: V, useType: RedissonUseType, func: () -> R): R = func()
}

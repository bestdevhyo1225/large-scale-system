package com.hyoseok.service

import com.hyoseok.aspect.annotation.RedissonDistributedLock
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedissonDistributedLockService {

    @RedissonDistributedLock(waitTime = 3_000, leaseTime = 3_000, timeUnit = TimeUnit.MILLISECONDS)
    fun <T : Any> executeWithLock(shortUrl: String, func: () -> T): T = func()
}

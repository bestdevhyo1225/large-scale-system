package com.hyoseok.aspect

import com.hyoseok.aspect.annotation.RedissonDistributedLock
import com.hyoseok.config.RedissonKeys
import com.hyoseok.exception.DistributedLockAcquisitionTimeoutException
import com.hyoseok.exception.RedissonDistributedLockExceptionMessage
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
class RedissonDistributedLockAspect(
    private val redissonClient: RedissonClient,
) {

    private val logger = KotlinLogging.logger {}

    @Around("execution(* com.hyoseok.service.RedissonDistributedLockService.executeWithLock(..)) && @annotation(distributedLock)")
    fun execute(joinPoint: ProceedingJoinPoint, distributedLock: RedissonDistributedLock): Any {
        val lockName = getLockName(joinPoint = joinPoint)
        val rLock = getLock(lockName = lockName)

        tryLock(
            rLock = rLock,
            lockName = lockName,
            waitTime = distributedLock.waitTime,
            leaseTime = distributedLock.leaseTime,
            timeUnit = distributedLock.timeUnit,
        )

        try {
            return joinPoint.proceed()
        } finally {
            releaseLock(rLock = rLock, lockName = lockName)
        }
    }

    private fun getLockName(joinPoint: ProceedingJoinPoint) =
        RedissonKeys.getShortUrlKey(shortUrl = joinPoint.args.first().toString())

    private fun getLock(lockName: String): RLock {
        return redissonClient.getLock(lockName)
            .apply { logger.info { "getLock (key = {$lockName})" } }
    }

    private fun tryLock(rLock: RLock, lockName: String, waitTime: Long, leaseTime: Long, timeUnit: TimeUnit) {
        val isAcquiredLock: Boolean

        try {
            isAcquiredLock = rLock.tryLock(waitTime, leaseTime, timeUnit)
        } catch (exception: InterruptedException) {
            throw RuntimeException(RedissonDistributedLockExceptionMessage.TRY_LOCK_INTERRUPT_FAILURE)
        }

        if (!isAcquiredLock) {
            throw DistributedLockAcquisitionTimeoutException(
                message = RedissonDistributedLockExceptionMessage.LOCK_ACQUISITION_FAILURE,
            )
        }

        logger.info { "tryLock (key = {$lockName})" }
    }

    private fun releaseLock(rLock: RLock, lockName: String) {
        if (rLock.isLocked && rLock.isHeldByCurrentThread) {
            logger.info { "releaseLock (key = {$lockName})" }
            return rLock.unlock()
        }

        logger.info { "Already releaseLock (key = $lockName)" }
    }
}

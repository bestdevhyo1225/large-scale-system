package com.hyoseok.aspect

import com.hyoseok.aspect.RedissonDistributedLockAspect.PointcutExpressions.SIGNATURE_1
import com.hyoseok.aspect.RedissonDistributedLockAspect.PointcutExpressions.SIGNATURE_2
import com.hyoseok.aspect.annotation.RedissonDistributedLock
import com.hyoseok.config.RedissonKeys
import com.hyoseok.config.RedissonUseType
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

    object PointcutExpressions {
        const val SIGNATURE_1 = "execution(* com.hyoseok.service.RedissonDistributedLockService.executeWithLock(..))"
        const val SIGNATURE_2 = "@annotation(distributedLock)"
    }

    @Around("$SIGNATURE_1 && $SIGNATURE_2")
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

    private fun getLockName(joinPoint: ProceedingJoinPoint): String {
        val (first, second) = joinPoint.args
        return when (second as RedissonUseType) {
            RedissonUseType.URL -> RedissonKeys.getLockUrlKey(value = first.toString())
        }
    }

    private fun getLock(lockName: String): RLock =
        redissonClient.getLock(lockName).apply { logger.info { "getLock ($lockName)" } }

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

        logger.info { "tryLock ($lockName)" }
    }

    private fun releaseLock(rLock: RLock, lockName: String) {
        if (rLock.isLocked && rLock.isHeldByCurrentThread) {
            logger.info { "releaseLock ($lockName)" }
            return rLock.unlock()
        }

        logger.info { "Already releaseLock ($lockName)" }
    }
}

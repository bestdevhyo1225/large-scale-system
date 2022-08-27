package com.hyoseok.exception

object RedissonDistributedLockExceptionMessage {
    const val LOCK_ACQUISITION_FAILURE = "일시적으로 작업을 수행할 수 없습니다. 잠시 후에 다시 시도해주세요."
    const val TRY_LOCK_INTERRUPT_FAILURE = "잠금을 획득하는 과정에서 예외로 인해 작업이 중단되었습니다."
}

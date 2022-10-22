package com.hyoseok.coupon.repository

import com.hyoseok.coupon.entity.CouponIssuedLog
import java.time.LocalDateTime

interface CouponIssuedLogRepository {
    fun deleteAllByCreatedAtBefore(createdAt: LocalDateTime)
    fun save(couponIssuedLog: CouponIssuedLog)
    fun saveAll(couponIssuedLogs: List<CouponIssuedLog>)
    fun updateIsSendCompleted(couponId: Long, memberId: Long, isSendCompleted: Boolean)
}

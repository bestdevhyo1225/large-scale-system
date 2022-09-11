package com.hyoseok.sns.repository

import com.hyoseok.sns.entity.SnsCache
import java.util.concurrent.TimeUnit

interface SnsCacheRepository {
    fun setex(key: String, value: SnsCache, expireTime: Long, timeUnit: TimeUnit)
    fun zaddSnsKeys(key: String, value: String, score: Double)
}

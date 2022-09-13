package com.hyoseok.sns.repository

import com.hyoseok.sns.entity.SnsCache
import java.util.concurrent.TimeUnit

interface SnsCacheRepository {
    fun setex(key: String, value: SnsCache, expireTime: Long, timeUnit: TimeUnit)
    fun setAllEx(keysAndValues: List<Pair<String, SnsCache>>, expireTime: Long, timeUnit: TimeUnit)
    fun zaddString(key: String, value: String, score: Double)

    fun zremString(key: String, value: String)
    fun zremStringRangeByRank(key: String, startIndex: Long, endIndex: Long)
}

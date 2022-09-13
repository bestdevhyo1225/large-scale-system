package com.hyoseok.sns.repository.read

import com.hyoseok.sns.entity.SnsCache

interface SnsCacheReadRepository {
    fun get(key: String, clazz: Class<SnsCache>): SnsCache?
    fun mget(keys: List<String>, clazz: Class<SnsCache>): List<SnsCache>
    fun zrevrangeString(key: String, start: Long, end: Long): List<String>
    fun zcard(key: String): Long
}

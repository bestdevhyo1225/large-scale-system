package com.hyoseok.config

import java.sql.Timestamp
import java.time.LocalDateTime

object RedisZsetScores {
    fun getTimestampCreatedAt(createdAt: LocalDateTime) = Timestamp.valueOf(createdAt).time.toDouble()
}

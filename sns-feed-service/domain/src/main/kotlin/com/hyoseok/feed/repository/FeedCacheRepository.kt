package com.hyoseok.feed.repository

interface FeedCacheRepository {
    fun <T : Any> zadd(key: String, value: T, score: Double)
    fun zremRangeByRank(key: String, start: Long, end: Long)

    fun zremRangeByScore(key: String, min: Double, max: Double)
    fun zremRangeByScoreUsedPipeline(keysAndScores: List<Triple<String, Double, Double>>)
}

package com.hyoseok.wish.repository

interface WishRedisPipelineRepository {
    fun getWishCountsMap(postIds: List<Long>): Map<Long, Long>
}

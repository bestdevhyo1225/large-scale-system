package com.hyoseok.wish.repository

interface WishRedisPipelineRepository {
    fun getWishCount(postIds: List<Long>): Map<Long, Long>
}

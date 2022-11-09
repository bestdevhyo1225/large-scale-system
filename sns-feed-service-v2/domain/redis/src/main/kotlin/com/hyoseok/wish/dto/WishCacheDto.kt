package com.hyoseok.wish.dto

import com.hyoseok.wish.entity.WishCache

data class WishCacheDto(
    val postId: Long,
    val memberId: Long,
) {
    fun toEntity() = WishCache(postId = postId, memberId = memberId)
}

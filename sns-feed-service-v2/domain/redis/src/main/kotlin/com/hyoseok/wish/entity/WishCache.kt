package com.hyoseok.wish.entity

data class WishCache(
    val postId: Long,
    val memberId: Long,
) {

    val expireTime: Long = 60 * 30 // 30분

    companion object {
        fun getWishPostKey(postId: Long) = "wish:post:$postId"
    }

    fun getWishPostKey() = getWishPostKey(postId = postId)
}

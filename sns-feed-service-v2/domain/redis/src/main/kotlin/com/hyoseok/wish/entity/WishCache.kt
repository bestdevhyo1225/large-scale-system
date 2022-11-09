package com.hyoseok.wish.entity

data class WishCache(
    val postId: Long,
    val memberId: Long,
) {

    val expireTime: Long = 60 * 60 * 3 // 3시간

    companion object {
        fun getKey(postId: Long) = "post:$postId:wishes"
    }

    fun getKey() = getKey(postId = postId)
}

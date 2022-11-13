package com.hyoseok.post.entity

import java.time.LocalDateTime

data class PostCache(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImageCache>,
) {

    companion object {
        const val POST_KEYS = "post:keys"
        const val ZSET_POST_MAX_LIMIT = -100_001L

        fun getPostIdKey(id: Long) = "post:$id"
        fun getPostIdViewsKey(id: Long) = "post:$id:views"

        fun getPostKeyAndExpireTime(id: Long) = Pair(first = getPostIdKey(id = id), second = 60 * 60 * 3L) // 3시간
        fun getPostViewKeyAndExpireTime(id: Long) = Pair(first = getPostIdViewsKey(id = id), second = 60 * 5L) // 5분
    }
}
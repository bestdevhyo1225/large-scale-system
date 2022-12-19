package com.hyoseok.post.entity

import java.time.LocalDateTime

data class PostCache(
    val id: Long,
    val memberId: Long,
    val title: String,
    val contents: String,
    val writer: String,
    val viewCount: Long,
    val wishCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val images: List<PostImageCache>,
) {

    companion object {
        private const val HASH_MAX_ENTRIES = 1_000L

        const val POST_MEMBER_MAX_LIMIT = -10_001L

        fun getPostBucketKey(id: Long) = "post:bucket:${id.div(HASH_MAX_ENTRIES)}"
        fun getPostViewBucketKey(id: Long) = "post:view:bucket:${id.div(HASH_MAX_ENTRIES)}"
        fun getPostMemberIdBucketKey(memberId: Long) = "post:memberid:bucket:${memberId.div(HASH_MAX_ENTRIES)}"
        fun getPostIdsByMemberIdKey(memberId: Long) = "post:ids:member:$memberId"
        fun getPostIdKey(id: Long) = "post:$id"
        fun getPostKeyAndExpireTime(id: Long) = Pair(first = getPostIdKey(id = id), second = 60L * 30L) // 30분
    }
}

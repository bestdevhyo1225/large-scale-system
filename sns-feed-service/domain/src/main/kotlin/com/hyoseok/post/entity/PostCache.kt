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
    val images: List<PostImage>,
)

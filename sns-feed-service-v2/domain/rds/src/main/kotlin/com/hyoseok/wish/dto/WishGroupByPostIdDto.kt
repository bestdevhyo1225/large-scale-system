package com.hyoseok.wish.dto

import com.querydsl.core.annotations.QueryProjection

data class WishGroupByPostIdDto @QueryProjection constructor(
    val postId: Long,
    val wishCount: Long,
)

package com.hyoseok.wish.repository

import com.hyoseok.wish.dto.WishGroupByPostIdDto
import com.hyoseok.wish.entity.Wish

interface WishReadRepository {

    fun countByPostId(postId: Long): Long
    fun countGroupByPostIds(postIds: List<Long>): List<WishGroupByPostIdDto>
    fun findById(id: Long): Wish
}

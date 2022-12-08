package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishGroupByPostIdDto
import com.hyoseok.wish.repository.WishReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WishReadService(
    private val wishReadRepository: WishReadRepository,
) {

    fun getCountByPostId(postId: Long): Long = wishReadRepository.countByPostId(postId = postId)

    fun getCountsByPostIds(postIds: List<Long>): Map<Long, WishGroupByPostIdDto> =
        if (postIds.isNotEmpty()) {
            wishReadRepository.countGroupByPostIds(postIds = postIds)
                .associateBy { it.postId }
        } else {
            mapOf()
        }
}

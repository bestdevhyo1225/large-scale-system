package com.hyoseok.wish.service

import com.hyoseok.wish.repository.WishReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WishReadService(
    private val wishReadRepository: WishReadRepository,
) {

    fun getCountByPostId(postId: Long): Long = wishReadRepository.countByPostId(postId = postId)

    fun getCountsByPostIds(postIds: List<Long>): Map<Long, Long> =
        if (postIds.isNotEmpty()) {
            wishReadRepository.countGroupByPostIds(postIds = postIds)
                .associate { it.postId to it.wishCount }
        } else {
            mapOf()
        }
}

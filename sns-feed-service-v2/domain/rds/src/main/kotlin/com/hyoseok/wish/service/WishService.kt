package com.hyoseok.wish.service

import com.hyoseok.wish.dto.WishDto
import com.hyoseok.wish.entity.Wish
import com.hyoseok.wish.repository.WishRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WishService(
    private val wishRepository: WishRepository,
) {

    fun create(postId: Long, memberId: Long): WishDto {
        val savedWish: Wish = wishRepository.save(Wish(postId = postId, memberId = memberId))
        return with(receiver = savedWish) {
            WishDto(id = id!!, postId = postId, memberId = memberId, createdAt = createdAt)
        }
    }
}

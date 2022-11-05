package com.hyoseok.post.service

import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.repository.PostReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostReadService(
    private val postReadRepository: PostReadRepository,
) {

    fun findPost(id: Long) = PostDto(post = postReadRepository.findById(id = id))
}

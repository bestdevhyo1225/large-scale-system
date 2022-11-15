package com.hyoseok.post.service

import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.util.PageRequestByPosition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostReadService(
    private val postReadRepository: PostReadRepository,
) {

    fun findPost(id: Long) = PostDto(post = postReadRepository.findById(id = id))

    fun findPosts(ids: List<Long>): List<PostDto> {
        if (ids.isEmpty()) {
            return listOf()
        }
        return postReadRepository.findAllByInId(ids = ids).map { PostDto(post = it) }
    }

    fun findPosts(memberIds: List<Long>, pageRequestByPosition: PageRequestByPosition): List<PostDto> {
        if (memberIds.isEmpty()) {
            return listOf()
        }

        val (start: Long, size: Long) = pageRequestByPosition

        if (start <= -1L || size == 0L) {
            return listOf()
        }

        return postReadRepository
            .findAllByMemberIdsAndLimitAndCount(memberIds = memberIds, limit = size, offset = start)
            .map { PostDto(post = it) }
    }
}

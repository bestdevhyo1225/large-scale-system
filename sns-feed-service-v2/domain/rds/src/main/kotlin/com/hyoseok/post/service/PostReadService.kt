package com.hyoseok.post.service

import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.entity.Post.Companion.POST_IDS_LIMIT_SIZE
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class PostReadService(
    private val postReadRepository: PostReadRepository,
) {

    fun findPost(id: Long) = PostDto(post = postReadRepository.findById(id = id))

    fun findPostDetail(id: Long) = PostDto(post = postReadRepository.findByIdWithPostImage(id = id))

    fun findPosts(ids: List<Long>): List<PostDto> {
        if (ids.isEmpty()) {
            return listOf()
        }

        val postIds: List<Long> =
            if (ids.size > POST_IDS_LIMIT_SIZE) {
                ids.slice(0..999)
            } else {
                ids
            }

        return postReadRepository.findAllByInId(ids = postIds)
            .map { PostDto(post = it) }
    }

    fun findPosts(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (start: Long, size: Long) = pageRequestByPosition
        val postDtos: List<PostDto> = postReadRepository
            .findAllByMemberIdAndLimitAndOffset(memberId = memberId, limit = size, offset = start)
            .map { PostDto(post = it) }

        return PageByPosition(
            items = postDtos,
            nextPageRequestByPosition = pageRequestByPosition.next(itemSize = postDtos.size),
        )
    }

    fun findPosts(memberIds: List<Long>, pageRequestByPosition: PageRequestByPosition): List<PostDto> {
        if (memberIds.isEmpty()) {
            return listOf()
        }

        val (start: Long, size: Long) = pageRequestByPosition

        if (start < 0L || size == 0L) {
            return listOf()
        }

        val toCreatedAt: LocalDateTime = LocalDateTime.now().withNano(0)
        val fromCreatedAt: LocalDateTime = toCreatedAt.minusDays(1)

        return postReadRepository.findAllByMemberIdsAndCreatedAtAndLimitAndOffset(
            memberIds = memberIds,
            fromCreatedAt = fromCreatedAt,
            toCreatedAt = toCreatedAt,
            limit = size,
            offset = start,
        ).map { PostDto(post = it) }
    }
}

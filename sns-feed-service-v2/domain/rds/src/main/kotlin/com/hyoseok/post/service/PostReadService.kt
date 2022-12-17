package com.hyoseok.post.service

import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.entity.Post.Companion.POST_IDS_LIMIT_SIZE
import com.hyoseok.post.repository.PostReadRepository
import com.hyoseok.util.PageByPosition
import com.hyoseok.util.PageRequestByPosition
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostReadService(
    private val postReadRepository: PostReadRepository,
) {

    private val logger = KotlinLogging.logger {}

    fun findPost(id: Long) = PostDto(post = postReadRepository.findById(id = id))

    fun findPosts(ids: List<Long>): List<PostDto> {
        if (ids.isEmpty()) {
            return listOf()
        }

        if (ids.size > POST_IDS_LIMIT_SIZE) {
            logger.info { "ids.size 값이 1000을 넘었기 때문에 1,000개만 잘라서 조회합니다" }
            return postReadRepository.findAllByInId(ids = ids.slice(0..999))
                .map { PostDto(post = it) }
        }

        return postReadRepository.findAllByInId(ids = ids)
            .map { PostDto(post = it) }
    }

    fun findPosts(memberId: Long, pageRequestByPosition: PageRequestByPosition): PageByPosition<PostDto> {
        val (start: Long, size: Long) = pageRequestByPosition

        if (start <= -1L || size == 0L) {
            return PageByPosition(items = listOf(), nextPageRequestByPosition = pageRequestByPosition)
        }

        val postDtos: List<PostDto> = postReadRepository
            .findAllByMemberIdAndLimitAndCount(memberId = memberId, limit = size, offset = start)
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

        return postReadRepository
            .findAllByMemberIdsAndLimitAndCount(memberIds = memberIds, limit = size, offset = start)
            .map { PostDto(post = it) }
    }
}

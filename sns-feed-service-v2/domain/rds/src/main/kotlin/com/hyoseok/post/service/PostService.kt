package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCreateDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostReadRepositoryImpl.ErrorMessage.NOT_FOUND_POST
import com.hyoseok.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
) {

    fun create(dto: PostCreateDto): PostDto =
        with(receiver = dto) {
            Post(
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                postImages = images.map { PostImage(url = it.url, sortOrder = it.sortOrder) },
            )
        }
            .run { postRepository.save(this) }
            .let { PostDto(post = it) }

    fun incrementViewCount(id: Long) {
        val post: Post = postRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException(NOT_FOUND_POST)
        post.increaseViewCount(viewCount = 1)
    }
}

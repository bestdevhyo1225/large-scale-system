package com.hyoseok.post.service

import com.hyoseok.post.dto.PostCreateDto
import com.hyoseok.post.dto.PostDto
import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.PostImage
import com.hyoseok.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
) {

    fun create(dto: PostCreateDto): PostDto {
        return with(receiver = dto) {
            Post(
                memberId = memberId,
                title = title,
                contents = contents,
                writer = writer,
                postImages = images.map { PostImage(url = it.url, sortOrder = it.sortOrder) },
            )
        }
            .also { postRepository.save(it) }
            .let { PostDto(post = it) }
    }
}

package com.hyoseok.repository.post

import com.hyoseok.entity.post.PostJpaEntity
import com.hyoseok.post.entity.Post
import com.hyoseok.post.repository.PostRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class PostRepositoryImpl(
    private val postJpaRepository: PostJpaRepository,
) : PostRepository {

    override fun save(post: Post) {
        val postJpaEntity = PostJpaEntity(post = post)
        postJpaRepository.save(postJpaEntity)
        return postJpaEntity.mapDomainEntity(post = post)
    }

    override fun update(post: Post) {
        TODO("Not yet implemented")
    }
}

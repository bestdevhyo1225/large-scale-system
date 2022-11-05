package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.QPost.post
import com.hyoseok.post.entity.QPostImage.postImage
import com.hyoseok.post.repository.PostReadRepositoryImpl.ErrorMessage.NOT_FOUND_POST
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PostReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val postRepository: PostRepository,
) : PostReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_POST = "게시글을 찾을 수 없습니다"
    }

    override fun findById(id: Long): Post =
        jpaQueryFactory
            .selectFrom(post)
            .where(postIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

    override fun findByIdWithPostImage(id: Long): Post =
        jpaQueryFactory
            .selectFrom(post)
            .innerJoin(post.postImages, postImage).fetchJoin()
            .where(postIdEq(id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)
}

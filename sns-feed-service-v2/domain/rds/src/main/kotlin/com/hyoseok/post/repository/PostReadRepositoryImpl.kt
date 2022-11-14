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

    override fun findAllByInId(ids: List<Long>): List<Post> =
        jpaQueryFactory
            .selectFrom(post)
            .where(postIdIn(ids = ids))
            .fetch()

    override fun findAllByMemberIdsAndLimitAndCount(memberIds: List<Long>, limit: Long, offset: Long): List<Post> =
        jpaQueryFactory
            .selectFrom(post)
            .where(postMemberIdIn(memberIds = memberIds))
            .limit(limit)
            .offset(offset)
            .fetch()

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)
    private fun postIdIn(ids: List<Long>): BooleanExpression = post.id.`in`(ids)
    private fun postMemberIdIn(memberIds: List<Long>): BooleanExpression = post.memberId.`in`(memberIds)
}

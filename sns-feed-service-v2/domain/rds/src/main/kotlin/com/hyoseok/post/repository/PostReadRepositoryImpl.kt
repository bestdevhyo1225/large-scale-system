package com.hyoseok.post.repository

import com.hyoseok.post.entity.Post
import com.hyoseok.post.entity.QPost.post
import com.hyoseok.post.entity.QPostImage.postImage
import com.hyoseok.post.repository.PostReadRepositoryImpl.ErrorMessage.NOT_FOUND_POST
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
@Transactional(readOnly = true)
class PostReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : PostReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_POST = "게시글을 찾을 수 없습니다"
    }

    override fun findById(id: Long): Post =
        jpaQueryFactory
            .selectFrom(post)
            .where(
                postIdEq(id = id),
                postDeletedAtIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

    override fun findByIdWithPostImage(id: Long): Post =
        jpaQueryFactory
            .selectFrom(post)
            .innerJoin(post.postImages, postImage).fetchJoin()
            .where(
                postIdEq(id),
                postDeletedAtIsNull(),
            )
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

    override fun findAllByInId(ids: List<Long>): List<Post> =
        jpaQueryFactory
            .selectFrom(post).distinct()
            .innerJoin(post.postImages, postImage).fetchJoin()
            .where(
                postIdIn(ids = ids),
                postDeletedAtIsNull(),
            )
            .orderBy(postIdDesc())
            .fetch()

    override fun findAllByMemberIdAndLimitAndOffset(memberId: Long, limit: Long, offset: Long): List<Post> =
        jpaQueryFactory
            .selectFrom(post)
            .where(
                postMemberIdEq(memberId = memberId),
                postDeletedAtIsNull(),
            )
            .orderBy(postIdDesc())
            .limit(limit)
            .offset(offset)
            .fetch()

    override fun findAllByMemberIdsAndCreatedAtAndLimitAndOffset(
        memberIds: List<Long>,
        fromCreatedAt: LocalDateTime,
        toCreatedAt: LocalDateTime,
        limit: Long,
        offset: Long,
    ): List<Post> =
        jpaQueryFactory
            .selectFrom(post)
            .where(
                postMemberIdIn(memberIds = memberIds),
                postCreatedAtBetween(fromCreatedAt, toCreatedAt),
                postDeletedAtIsNull(),
            )
            .orderBy(postIdDesc())
            .limit(limit)
            .offset(offset)
            .fetch()

    private fun postIdEq(id: Long): BooleanExpression = post.id.eq(id)

    private fun postIdIn(ids: List<Long>): BooleanExpression = post.id.`in`(ids)

    private fun postMemberIdEq(memberId: Long): BooleanExpression = post.memberId.eq(memberId)

    private fun postMemberIdIn(memberIds: List<Long>): BooleanExpression = post.memberId.`in`(memberIds)

    private fun postDeletedAtIsNull(): BooleanExpression = post.deletedAt.isNull

    private fun postCreatedAtBetween(fromCreatedAt: LocalDateTime, toCreatedAt: LocalDateTime): BooleanExpression =
        post.createdAt.between(fromCreatedAt, toCreatedAt)

    private fun postIdDesc(): OrderSpecifier<Long> = post.id.desc()
}

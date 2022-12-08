package com.hyoseok.wish.repository

import com.hyoseok.wish.dto.QWishGroupByPostIdDto
import com.hyoseok.wish.dto.WishGroupByPostIdDto
import com.hyoseok.wish.entity.QWish.wish
import com.hyoseok.wish.entity.Wish
import com.hyoseok.wish.repository.WishReadRepositoryImpl.ErrorMessage.NOT_FOUND_WISH
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class WishReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : WishReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_WISH = "좋아요 정보를 찾을 수 없습니다"
    }

    override fun countByPostId(postId: Long): Long =
        jpaQueryFactory
            .select(wish.count())
            .from(wish)
            .where(wishPostIdEq(postId = postId))
            .fetchOne() ?: 0L

    override fun countGroupByPostIds(postIds: List<Long>): List<WishGroupByPostIdDto> =
        jpaQueryFactory
            .select(
                QWishGroupByPostIdDto(
                    wish.postId,
                    wish.memberId.count().`as`("wishCount"),
                ),
            )
            .from(wish)
            .where(wishPostIdsEq(postIds = postIds))
            .groupBy(wish.postId)
            .fetch()

    override fun findById(id: Long): Wish =
        jpaQueryFactory
            .selectFrom(wish)
            .where(wishIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_WISH)

    private fun wishIdEq(id: Long): BooleanExpression = wish.id.eq(id)
    private fun wishPostIdEq(postId: Long): BooleanExpression = wish.postId.eq(postId)
    private fun wishPostIdsEq(postIds: List<Long>): BooleanExpression = wish.postId.`in`(postIds)
}

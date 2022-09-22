package com.hyoseok.repository.post

import com.hyoseok.entity.post.PostJpaEntity
import com.hyoseok.entity.post.QPostImageJpaEntity.postImageJpaEntity
import com.hyoseok.entity.post.QPostJpaEntity.postJpaEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_POST
import com.hyoseok.post.entity.Post
import com.hyoseok.post.repository.PostReadRepository
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
    private val postJpaRepository: PostJpaRepository,
) : PostReadRepository {

    override fun findById(id: Long): Post {
        val postJpaEntity: PostJpaEntity = jpaQueryFactory
            .selectFrom(postJpaEntity)
            .where(postJpaEntityIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

        return postJpaEntity.toDomainEntity()
    }

    override fun findByIdWithImages(id: Long): Post {
        val postJpaEntity: PostJpaEntity = jpaQueryFactory
            .selectFrom(postJpaEntity)
            .innerJoin(postJpaEntity.postImageJpaEntities, postImageJpaEntity)
            .where(postJpaEntityIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_POST)

        return postJpaEntity.toDomainEntity(isFetchPostImages = true)
    }

    override fun findRecentlyRegisteredAllByMemberIdAndPage(
        memberId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Post>> {
        val total: Long = postJpaRepository.countByMemberIdAndDeletedIsNull(memberId = memberId)
        val postJpaEntities: List<PostJpaEntity> = jpaQueryFactory
            .selectFrom(postJpaEntity)
            .where(postJpaEntityMemberIdEq(memberId = memberId))
            .orderBy(postJpaEntityCreatedAtDesc())
            .limit(limit)
            .offset(offset)
            .fetch()

        return Pair(
            first = total,
            second = postJpaEntities.map { it.toDomainEntity(isFetchPostImages = true) },
        )
    }

    override fun findRecentlyRegisteredAllByIds(ids: List<Long>): List<Post> {
        val postJpaEntities: List<PostJpaEntity> = jpaQueryFactory
            .selectFrom(postJpaEntity)
            .where(postJpaEntityIdIn(ids = ids))
            .orderBy(postJpaEntityCreatedAtDesc())
            .fetch()

        return postJpaEntities.map { it.toDomainEntity(isFetchPostImages = true) }
    }

    private fun postJpaEntityIdEq(id: Long): BooleanExpression = postJpaEntity.id.eq(id)
    private fun postJpaEntityIdIn(ids: List<Long>): BooleanExpression = postJpaEntity.id.`in`(ids)
    private fun postJpaEntityMemberIdEq(memberId: Long): BooleanExpression = postJpaEntity.memberId.eq(memberId)
    private fun postJpaEntityCreatedAtDesc(): OrderSpecifier<LocalDateTime> = postJpaEntity.createdAt.desc()
}

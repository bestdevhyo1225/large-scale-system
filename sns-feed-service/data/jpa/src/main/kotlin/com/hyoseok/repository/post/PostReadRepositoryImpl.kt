package com.hyoseok.repository.post

import com.hyoseok.entity.post.PostJpaEntity
import com.hyoseok.entity.post.QPostImageJpaEntity.postImageJpaEntity
import com.hyoseok.entity.post.QPostJpaEntity.postJpaEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_POST
import com.hyoseok.post.entity.Post
import com.hyoseok.post.repository.PostReadRepository
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class PostReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
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

    override fun findAllByPLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<Post>> {
        TODO("Not yet implemented")
    }

    override fun findRecentlyRegisteredAllByIds(ids: List<Long>): List<Post> {
        val postJpaEntities: List<PostJpaEntity> = jpaQueryFactory
            .selectFrom(postJpaEntity)
            .where(postJpaEntityIdIn(ids = ids))
            .orderBy(postJpaEntity.createdAt.desc())
            .fetch()

        return postJpaEntities.map { it.toDomainEntity(isFetchPostImages = true) }
    }

    private fun postJpaEntityIdEq(id: Long): BooleanExpression = postJpaEntity.id.eq(id)
    private fun postJpaEntityIdIn(ids: List<Long>): BooleanExpression = postJpaEntity.id.`in`(ids)
}

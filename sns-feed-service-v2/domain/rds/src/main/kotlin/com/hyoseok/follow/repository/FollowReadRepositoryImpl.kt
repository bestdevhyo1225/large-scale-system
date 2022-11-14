package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.QFollow.follow
import com.hyoseok.follow.repository.FollowReadRepositoryImpl.ErrorMessage.NOT_FOUND_FOLLOW
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class FollowReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val followRepository: FollowRepository,
) : FollowReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_FOLLOW = "팔로우 정보를 찾을 수 없습니다"
    }

    override fun countByFolloweeId(followeeId: Long): Long =
        followRepository.countByFolloweeId(followeeId = followeeId)

    override fun findById(id: Long): Follow =
        jpaQueryFactory
            .selectFrom(follow)
            .where(followIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_FOLLOW)

    override fun findAllByFolloweeIdAndLimitAndOffset(
        followeeId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> =
        Pair(
            first = followRepository.countByFolloweeId(followeeId = followeeId),
            second = jpaQueryFactory
                .selectFrom(follow)
                .where(followFolloweeIdEq(followeeId = followeeId))
                .limit(limit)
                .offset(offset)
                .fetch(),
        )

    override fun findAllByFollowerIdAndLimitAndOffset(
        followerId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> =
        Pair(
            first = followRepository.countByFollowerId(followerId = followerId),
            second = jpaQueryFactory
                .selectFrom(follow)
                .where(followFollowerIdEq(followerId = followerId))
                .limit(limit)
                .offset(offset)
                .fetch(),
        )

    override fun findAllByFollowerIdAndLimitOrderByIdDesc(
        followerId: Long,
        limit: Long,
    ): List<Follow> =
        jpaQueryFactory
            .selectFrom(follow)
            .where(followFollowerIdEq(followerId = followerId))
            .orderBy(followIdDesc())
            .limit(limit)
            .fetch()

    private fun followIdEq(id: Long): BooleanExpression = follow.id.eq(id)
    private fun followFolloweeIdEq(followeeId: Long): BooleanExpression = follow.followeeId.eq(followeeId)
    private fun followFollowerIdEq(followerId: Long): BooleanExpression = follow.followerId.eq(followerId)
    private fun followIdDesc(): OrderSpecifier<*> = follow.id.desc()
}

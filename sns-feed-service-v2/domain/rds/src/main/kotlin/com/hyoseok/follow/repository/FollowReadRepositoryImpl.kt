package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.entity.QFollow.follow
import com.hyoseok.follow.entity.QFollowCount.followCount
import com.hyoseok.follow.repository.FollowReadRepositoryImpl.ErrorMessage.NOT_FOUND_FOLLOW
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class FollowReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : FollowReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_FOLLOW = "팔로우 정보를 찾을 수 없습니다"
    }

    override fun countByFolloweeId(followeeId: Long): Long =
        jpaQueryFactory
            .select(followCount.totalFollower)
            .from(followCount)
            .where(followCountMemberIdEq(memberId = followeeId))
            .fetchOne() ?: 0L

    override fun countByFollowerId(followerId: Long): Long =
        jpaQueryFactory
            .select(followCount.totalFollowee)
            .from(followCount)
            .where(followCountMemberIdEq(memberId = followerId))
            .fetchOne() ?: 0L

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
            first = countByFolloweeId(followeeId = followeeId),
            second = jpaQueryFactory
                .selectFrom(follow)
                .where(followFolloweeIdEq(followeeId = followeeId))
                .limit(limit)
                .offset(offset)
                .fetch(),
        )

    override fun findAllByFolloweeIdAndLastIdAndLimit(followeeId: Long, lastId: Long, limit: Long): List<Follow> =
        jpaQueryFactory
            .selectFrom(follow)
            .where(
                followFolloweeIdEq(followeeId = followeeId),
                followIdGt(id = lastId),
            )
            .limit(limit)
            .fetch()

    override fun findAllByFollowerIdAndLimitAndOffset(
        followerId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> =
        Pair(
            first = countByFollowerId(followerId = followerId),
            second = jpaQueryFactory
                .selectFrom(follow)
                .where(followFollowerIdEq(followerId = followerId))
                .limit(limit)
                .offset(offset)
                .fetch(),
        )

    override fun findAllByFollowerIdAndLimitOrderByIdDesc(
        followerId: Long,
        checkTotalFollower: Long,
        limit: Long,
    ): List<Follow> =
        jpaQueryFactory
            .select(follow)
            .from(follow)
            .innerJoin(followCount).on(followFolloweeIdEq(followeeId = followCount.memberId))
            .where(
                followFollowerIdEq(followerId = followerId),
                followCountTotalFollowerGoe(totalFollower = checkTotalFollower),
            )
            .orderBy(followIdDesc())
            .limit(limit)
            .fetch()

    private fun followIdEq(id: Long): BooleanExpression = follow.id.eq(id)

    private fun followIdGt(id: Long): BooleanExpression? {
        if (id == 0L) {
            return null
        }
        return follow.id.gt(id)
    }

    private fun followFolloweeIdEq(followeeId: Long): BooleanExpression = follow.followeeId.eq(followeeId)

    private fun followFolloweeIdEq(followeeId: NumberPath<Long>): BooleanExpression = follow.followeeId.eq(followeeId)

    private fun followFollowerIdEq(followerId: Long): BooleanExpression = follow.followerId.eq(followerId)

    private fun followCountMemberIdEq(memberId: Long): BooleanExpression = followCount.memberId.eq(memberId)

    private fun followCountTotalFollowerGoe(totalFollower: Long): BooleanExpression =
        followCount.totalFollower.goe(totalFollower)

    private fun followIdDesc(): OrderSpecifier<*> = follow.id.desc()
}

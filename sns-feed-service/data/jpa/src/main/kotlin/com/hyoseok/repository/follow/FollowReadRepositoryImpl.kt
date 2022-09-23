package com.hyoseok.repository.follow

import com.hyoseok.entity.follow.FollowJpaEntity
import com.hyoseok.entity.follow.QFollowJpaEntity.followJpaEntity
import com.hyoseok.follow.entity.Follow
import com.hyoseok.follow.repository.FollowReadRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class FollowReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val followJpaRepository: FollowJpaRepository,
) : FollowReadRepository {

    override fun findAllByFollowerIdAndLimitAndOffset(
        followerId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> {
        TODO("Not yet implemented")
    }

    override fun findAllByFolloweeIdAndLimitAndOffset(
        followeeId: Long,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<Follow>> {
        val totalCount: Long = followJpaRepository.countByFolloweeId(followeeId = followeeId)
        val followJpaEntities: List<FollowJpaEntity> = jpaQueryFactory
            .selectFrom(followJpaEntity)
            .where(followJpaEntity.followeeId.eq(followeeId))
            .limit(limit)
            .offset(offset)
            .fetch()

        return Pair(first = totalCount, second = followJpaEntities.map { it.toDomainEntity() })
    }
}

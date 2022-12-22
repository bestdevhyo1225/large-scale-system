package com.hyoseok.follow.repository

import com.hyoseok.follow.entity.FollowCount
import com.hyoseok.follow.entity.QFollowCount.followCount
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class FollowCountReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : FollowCountReadRepository {

    override fun findByMemberId(memberId: Long): FollowCount? =
        jpaQueryFactory
            .selectFrom(followCount)
            .where(followCount.memberId.eq(memberId))
            .fetchOne()
}

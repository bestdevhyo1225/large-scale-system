package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberJpaEntity
import com.hyoseok.entity.member.QMemberJpaEntity.memberJpaEntity
import com.hyoseok.exception.DataJpaMessage.NOT_FOUND_MEMBER
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberReadRepository
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : MemberReadRepository {

    override fun findById(id: Long): Member {
        val memberJpaEntity: MemberJpaEntity = jpaQueryFactory
            .selectFrom(memberJpaEntity)
            .where(memberJpaEntityIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_MEMBER)

        return memberJpaEntity.toDomainEntity()
    }

    private fun memberJpaEntityIdEq(id: Long): BooleanExpression = memberJpaEntity.id.eq(id)
}
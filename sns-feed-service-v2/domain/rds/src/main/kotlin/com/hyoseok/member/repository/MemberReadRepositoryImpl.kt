package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member
import com.hyoseok.member.entity.Member.Companion.INFLUENCER_FALSE
import com.hyoseok.member.entity.Member.Companion.INFLUENCER_TRUE
import com.hyoseok.member.entity.QMember.member
import com.hyoseok.member.repository.MemberReadRepositoryImpl.ErrorMessage.NOT_FOUND_MEMBER
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : MemberReadRepository {

    object ErrorMessage {
        const val NOT_FOUND_MEMBER = "회원을 찾을 수 없습니다"
    }

    override fun findById(id: Long): Member =
        jpaQueryFactory
            .selectFrom(member)
            .where(memberIdEq(id = id))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_MEMBER)

    override fun findByInIdAndInfluencer(ids: List<Long>, influencer: Boolean): List<Member> =
        jpaQueryFactory
            .selectFrom(member)
            .where(
                memberIdsIn(ids = ids),
                memberInfluencerEq(influencer = influencer),
            )
            .fetch()

    private fun memberIdEq(id: Long): BooleanExpression = member.id.eq(id)
    private fun memberIdsIn(ids: List<Long>): BooleanExpression = member.id.`in`(ids)
    private fun memberInfluencerEq(influencer: Boolean): BooleanExpression {
        return member.influencer.eq(if (influencer) INFLUENCER_TRUE else INFLUENCER_FALSE)
    }
}

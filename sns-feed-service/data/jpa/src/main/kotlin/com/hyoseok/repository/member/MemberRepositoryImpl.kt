package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberJpaEntity
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {

    override fun save(member: Member) {
        val memberJpaEntity = MemberJpaEntity(member = member)
        memberJpaRepository.save(memberJpaEntity)
        memberJpaEntity.mapDomainEntity(member = member)
    }
}

package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberJpaEntity
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {

    override fun save(member: Member) {
        val memberJpaEntity = MemberJpaEntity(member = member)
        memberJpaRepository.save(memberJpaEntity)
        memberJpaEntity.mapDomainEntity(member = member)
    }
}

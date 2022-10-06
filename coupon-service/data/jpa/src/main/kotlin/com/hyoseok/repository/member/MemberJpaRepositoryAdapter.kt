package com.hyoseok.repository.member

import com.hyoseok.entity.member.MemberEntity
import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class MemberJpaRepositoryAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {

    override fun save(member: Member) {
        val memberEntity = MemberEntity(member = member)
        memberJpaRepository.save(memberEntity)
        member.changeId(id = memberEntity.id!!)
    }
}

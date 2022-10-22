package com.hyoseok.member.repository

import com.hyoseok.member.entity.Member
import com.hyoseok.member.entity.MemberEntity
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager")
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class MemberJpaRepositoryAdapter(
    private val memberJpaRepository: MemberJpaRepository,
) : MemberRepository {

    override fun save(member: Member) {
        memberJpaRepository.save(MemberEntity(member = member))
            .also { member.changeId(id = it.id!!) }
    }
}

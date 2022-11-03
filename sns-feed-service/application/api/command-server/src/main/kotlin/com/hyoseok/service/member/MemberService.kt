package com.hyoseok.service.member

import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.service.dto.MemberCreateDto
import com.hyoseok.service.dto.MemberCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(transactionManager = "jpaTransactionManager")
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun create(dto: MemberCreateDto): MemberCreateResultDto {
        val savedMember: Member = dto.toEntity().also { memberRepository.save(member = it) }
        return MemberCreateResultDto(member = savedMember)
    }
}

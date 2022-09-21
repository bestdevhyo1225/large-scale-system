package com.hyoseok.service.member

import com.hyoseok.member.entity.Member
import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.service.dto.MemberCreateDto
import com.hyoseok.service.dto.MemberCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun create(dto: MemberCreateDto): MemberCreateResultDto {
        val member: Member = dto.toEntity()
        memberRepository.save(member = member)
        return MemberCreateResultDto(member = member)
    }
}

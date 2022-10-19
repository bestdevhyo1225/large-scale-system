package com.hyoseok.member.service

import com.hyoseok.member.repository.MemberRepository
import com.hyoseok.member.service.dto.MemberCreateDto
import com.hyoseok.member.service.dto.MemberCreateResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun create(dto: MemberCreateDto) =
        MemberCreateResultDto(member = dto.toEntity().also { memberRepository.save(member = it) })
}

package com.hyoseok.member.service

import com.hyoseok.member.dto.MemberDto
import com.hyoseok.member.repository.MemberReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberReadService(
    private val memberReadRepository: MemberReadRepository,
) {

    fun findMember(id: Long): MemberDto =
        memberReadRepository.findById(id = id)
            .let { MemberDto(id = it.id!!, name = it.name, createdAt = it.createdAt) }
}

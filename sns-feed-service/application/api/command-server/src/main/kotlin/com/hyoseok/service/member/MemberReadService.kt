package com.hyoseok.service.member

import com.hyoseok.member.repository.MemberReadRepository
import com.hyoseok.service.dto.MemberFindResultDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
class MemberReadService(
    private val memberReadRepository: MemberReadRepository,
) {

    fun existsMember(id: Long): Boolean = memberReadRepository.exists(id = id)

    fun findMember(id: Long): MemberFindResultDto = MemberFindResultDto(member = memberReadRepository.findById(id = id))
}

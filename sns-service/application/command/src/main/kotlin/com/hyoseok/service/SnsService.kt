package com.hyoseok.service

import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsCreateResultDto
import com.hyoseok.service.dto.SnsFindResultDto
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.SnsRepository
import com.hyoseok.sns.repository.read.SnsReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SnsService(
    private val snsRepository: SnsRepository,
    private val snsReadRepository: SnsReadRepository,
) {

    @Transactional
    fun create(dto: SnsCreateDto): SnsCreateResultDto {
        val sns: Sns = dto.toEntity()
        snsRepository.save(sns = sns)
        return SnsCreateResultDto(sns = sns)
    }

    fun findWithAssociatedEntitiesById(snsId: Long) =
        SnsFindResultDto(sns = snsReadRepository.findWithAssociatedEntitiesById(snsId = snsId))
}

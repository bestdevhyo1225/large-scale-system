package com.hyoseok.service

import com.hyoseok.service.dto.SnsCreateDto
import com.hyoseok.service.dto.SnsEditDto
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.SnsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SnsCommandService(
    private val snsRepository: SnsRepository,
) {

    fun create(dto: SnsCreateDto): Sns {
        val sns: Sns = dto.toEntity()
        snsRepository.save(sns = sns)
        return sns
    }

    fun edit(dto: SnsEditDto): Sns {
        val sns: Sns = dto.toEntity()
        snsRepository.update(sns = sns)
        return sns
    }
}

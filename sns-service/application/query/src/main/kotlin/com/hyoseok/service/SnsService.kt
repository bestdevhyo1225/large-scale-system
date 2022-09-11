package com.hyoseok.service

import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.read.SnsReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SnsService(
    private val snsReadRepository: SnsReadRepository,
) {

    fun findWithAssociatedEntitiesById(snsId: Long): Sns =
        snsReadRepository.findWithAssociatedEntitiesById(snsId = snsId)

    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<List<Sns>, Long> =
        snsReadRepository.findAllByLimitAndOffset(limit = limit, offset = offset)
}

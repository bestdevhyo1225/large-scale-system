package com.hyoseok.sns.repository.read

import com.hyoseok.sns.entity.Sns

interface SnsReadRepository {
    fun findById(snsId: Long): Sns
    fun findWithAssociatedEntitiesById(snsId: Long): Sns
    fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<List<Sns>, Long>
}

package com.hyoseok.repository.sns

import com.hyoseok.entity.sns.SnsJpaEntity
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.SnsRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class SnsRepositoryImpl(
    private val snsJpaRepository: SnsJpaRepository,
) : SnsRepository {

    override fun save(sns: Sns) {
        val snsJpaEntity = SnsJpaEntity(sns = sns)
        snsJpaRepository.save(snsJpaEntity)
        snsJpaEntity.mapDomainEntity(sns = sns)
    }
}

package com.hyoseok.repository.sns.read

import com.hyoseok.entity.sns.QSnsImageJpaEntity.snsImageJpaEntity
import com.hyoseok.entity.sns.QSnsJpaEntity.snsJpaEntity
import com.hyoseok.entity.sns.QSnsTagJpaEntity.snsTagJpaEntity
import com.hyoseok.exception.Message.NOT_FOUND_SNS
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.read.SnsReadRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class SnsReadRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : SnsReadRepository {

    override fun findById(snsId: Long): Sns {
        val snsJpaEntity = jpaQueryFactory
            .selectFrom(snsJpaEntity)
            .where(snsJpaEntityIdEq(snsId = snsId))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_SNS)

        return snsJpaEntity.toDomainEntity()
    }

    override fun findWithAssociatedEntitiesById(snsId: Long): Sns {
        val snsJpaEntity = jpaQueryFactory
            .selectFrom(snsJpaEntity)
            .innerJoin(snsJpaEntity.snsImageJpaEntities, snsImageJpaEntity).fetchJoin()
            .where(snsJpaEntityIdEq(snsId = snsId))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_SNS)

        val snsTagJpaEntities = jpaQueryFactory
            .selectFrom(snsTagJpaEntity)
            .where(snsJpaEntityIdEq(snsId = snsId))
            .fetch()

        return snsJpaEntity.toDomainEntityAssociatedEntities(snsTagJpaEntities = snsTagJpaEntities)
    }

    private fun snsJpaEntityIdEq(snsId: Long) = snsJpaEntity.id.eq(snsId)
}

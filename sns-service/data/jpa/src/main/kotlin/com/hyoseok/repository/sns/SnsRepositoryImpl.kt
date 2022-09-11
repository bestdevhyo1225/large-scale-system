package com.hyoseok.repository.sns

import com.hyoseok.entity.sns.QSnsImageJpaEntity.snsImageJpaEntity
import com.hyoseok.entity.sns.QSnsJpaEntity.snsJpaEntity
import com.hyoseok.entity.sns.QSnsTagJpaEntity.snsTagJpaEntity
import com.hyoseok.entity.sns.SnsImageJpaEntity
import com.hyoseok.entity.sns.SnsJpaEntity
import com.hyoseok.entity.sns.SnsTagJpaEntity
import com.hyoseok.exception.Message.NOT_FOUND_SNS
import com.hyoseok.sns.entity.Sns
import com.hyoseok.sns.repository.SnsRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class SnsRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val snsJpaRepository: SnsJpaRepository,
    private val snsImageJpaRepository: SnsImageJpaRepository,
) : SnsRepository {

    override fun save(sns: Sns) {
        val snsJpaEntity = SnsJpaEntity(sns = sns)
        snsJpaRepository.save(snsJpaEntity)
        snsJpaEntity.mapDomainEntity(sns = sns)
    }

    override fun update(sns: Sns) {
        val snsJpaEntity: SnsJpaEntity = jpaQueryFactory
            .selectFrom(snsJpaEntity)
            .innerJoin(snsJpaEntity.snsImageJpaEntities, snsImageJpaEntity).fetchJoin()
            .where(snsJpaEntityIdEq(snsId = sns.id!!))
            .fetchOne() ?: throw NoSuchElementException(NOT_FOUND_SNS)

        val snsTagJpaEntity: SnsTagJpaEntity = jpaQueryFactory
            .selectFrom(snsTagJpaEntity)
            .where(snsJpaEntityIdEq(snsId = sns.id!!))
            .fetch()
            .first()

        snsJpaEntity.change(sns = sns)
        snsTagJpaEntity.change(snsTag = sns.snsTag!!)
        snsImageJpaRepository.deleteAllByIds(snsJpaEntity.snsImageJpaEntities.map { it.id!! })

        val snsImageJpaEntities: List<SnsImageJpaEntity> = sns.snsImages.map {
            val snsImageJpaEntity = SnsImageJpaEntity(snsImage = it)
            snsJpaEntity.addSnsImageJpaEntity(snsImageJpaEntity = snsImageJpaEntity)
            snsImageJpaEntity
        }

        snsImageJpaRepository.saveAll(snsImageJpaEntities)

        SnsImageJpaEntity.mapDomainEntities(snsImages = sns.snsImages, snsImageJpaEntities = snsImageJpaEntities)
    }

    private fun snsJpaEntityIdEq(snsId: Long) = snsJpaEntity.id.eq(snsId)
}

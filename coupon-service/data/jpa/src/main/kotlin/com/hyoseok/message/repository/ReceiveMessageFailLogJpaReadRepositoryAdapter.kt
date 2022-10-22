package com.hyoseok.message.repository

import com.hyoseok.message.entity.QReceiveMessageFailLogEntity.receiveMessageFailLogEntity
import com.hyoseok.message.entity.ReceiveMessageFailLog
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class ReceiveMessageFailLogJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val receiveMessageFailLogJpaRepository: ReceiveMessageFailLogJpaRepository,
) : ReceiveMessageFailLogReadRepository {

    override fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<ReceiveMessageFailLog>> =
        Pair(
            first = receiveMessageFailLogJpaRepository.countAll(),
            second = jpaQueryFactory
                .selectFrom(receiveMessageFailLogEntity)
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    override fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<ReceiveMessageFailLog>> =
        Pair(
            first = receiveMessageFailLogJpaRepository.countAllByInstanceId(instanceId = instanceId),
            second = jpaQueryFactory
                .selectFrom(receiveMessageFailLogEntity)
                .where(receiveMessageFailLogEntityEqInstanceId(instanceId = instanceId))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    private fun receiveMessageFailLogEntityEqInstanceId(instanceId: String): BooleanExpression =
        receiveMessageFailLogEntity.instanceId.eq(instanceId)
}

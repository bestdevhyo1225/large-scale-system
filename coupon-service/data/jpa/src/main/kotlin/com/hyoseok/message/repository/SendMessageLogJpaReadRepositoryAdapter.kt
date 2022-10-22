package com.hyoseok.message.repository

import com.hyoseok.message.entity.QSendMessageLogEntity.sendMessageLogEntity
import com.hyoseok.message.entity.SendMessageLog
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(transactionManager = "jpaTransactionManager", readOnly = true)
@ConditionalOnProperty(prefix = "data.enable", name = ["jpa"], havingValue = "true")
class SendMessageLogJpaReadRepositoryAdapter(
    private val jpaQueryFactory: JPAQueryFactory,
    private val sendMessageLogJpaRepository: SendMessageLogJpaRepository,
) : SendMessageLogReadRepository {

    override fun findAllByLimitAndOffset(limit: Long, offset: Long): Pair<Long, List<SendMessageLog>> =
        Pair(
            first = sendMessageLogJpaRepository.countAll(),
            second = jpaQueryFactory
                .selectFrom(sendMessageLogEntity)
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    override fun findAllByInstanceIdAndLimitAndOffset(
        instanceId: String,
        limit: Long,
        offset: Long,
    ): Pair<Long, List<SendMessageLog>> =
        Pair(
            first = sendMessageLogJpaRepository.countAllByInstanceId(instanceId = instanceId),
            second = jpaQueryFactory
                .selectFrom(sendMessageLogEntity)
                .where(sendMessageLogEntityEqInstanceId(instanceId = instanceId))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map { it.toDomain() },
        )

    private fun sendMessageLogEntityEqInstanceId(instanceId: String): BooleanExpression =
        sendMessageLogEntity.instanceId.eq(instanceId)
}
